package com.example.focus.Service;

import com.example.focus.ApiResponse.ApiException;
import com.example.focus.DTO.ProfileDTOin;
import com.example.focus.Model.Media;
import com.example.focus.Model.MyUser;
import com.example.focus.Model.ProfileEditor;
import com.example.focus.Model.ProfilePhotographer;
import com.example.focus.Repository.MediaRepository;
import com.example.focus.Repository.MyUserRepository;
import com.example.focus.Repository.ProfileEditorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProfileEditorService {

    private final ProfileEditorRepository profileEditorRepository;
    private final MyUserRepository myUserRepository;
    private final MediaRepository mediaRepository;
    private final com.example.focus.service.S3Service s3Service;
    private final String bucketName = "my-app-media-bucket";
    private final String region = "ap-northeast-1";

    public ProfileEditorService(ProfileEditorRepository profileEditorRepository, MyUserRepository myUserRepository, MediaRepository mediaRepository, com.example.focus.service.S3Service s3Service) {
        this.profileEditorRepository = profileEditorRepository;
        this.myUserRepository = myUserRepository;
        this.mediaRepository = mediaRepository;
        this.s3Service = s3Service;
    }

    public List<ProfileEditor> getAllEditorsProfiles() {
        List<ProfileEditor> profiles = profileEditorRepository.findAll();

        for (ProfileEditor profile : profiles) {
            if (profile.getImageURL() != null && !profile.getImageURL().startsWith("http")) {
                profile.setImageURL(buildS3Url(profile.getImageURL()));
            }
        }

        return profiles;
    }

    public ProfileEditor getMyProfile(Integer editorId) {
        ProfileEditor profile = profileEditorRepository.findProfileEditorById(editorId);
        if (profile == null) {
            throw new ApiException("Editor profile not found");
        }

        if (profile.getImageURL() != null && !profile.getImageURL().startsWith("http")) {
            profile.setImageURL(buildS3Url(profile.getImageURL()));
        }

        return profile;
    }

    public ProfileEditor getSpecificProfile(Integer userid1, Integer userid2) {
        MyUser user1 = myUserRepository.findMyUserById(userid1);
        if (user1 == null) {
            throw new ApiException("User not found");
        }

        MyUser user2 = myUserRepository.findMyUserById(userid2);
        if (user2 == null) {
            throw new ApiException("User you search about not found");
        }

        ProfileEditor profileEditor = profileEditorRepository.findProfileEditorById(userid2);
        if (profileEditor != null && profileEditor.getImageURL() != null && !profileEditor.getImageURL().startsWith("http")) {
            String s3Url = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + profileEditor.getImageURL();
            profileEditor.setImageURL(s3Url);
        }

        return profileEditor;
    }

    public void updateEditorProfile(Integer editorId, ProfileDTOin profileDTOin, MultipartFile file) throws IOException {
        ProfileEditor profile = profileEditorRepository.findProfileEditorById(editorId);
        if (profile == null) {
            throw new ApiException("Editor profile not found");
        }

        if (!isValidImageFile(file)) {
            throw new ApiException("Invalid image file. Only JPG, PNG, and JPEG files are allowed");
        }

        String s3FileName = s3Service.uploadFile(file);
        profile.setDescription(profileDTOin.getDescription());
        profile.setImageURL(s3FileName);

        profileEditorRepository.save(profile);
    }

    public void uploadEditorMedia(Integer editorId, MultipartFile file) throws IOException {
        ProfileEditor profile = profileEditorRepository.findProfileEditorById(editorId);
        if (profile == null) {
            throw new ApiException("Editor profile not found");
        }

        String fileName = file.getOriginalFilename();
        String fileType = getFileType(fileName);

        if ("unknown".equals(fileType)) {
            throw new ApiException("Unsupported file type. Only images and videos are allowed");
        }

        String s3FileName = s3Service.uploadFile(file);

        Media media = new Media();
        media.setProfileEditor(profile);
        media.setMediaType(fileType);
        media.setUploadDate(LocalDateTime.now());
        media.setMediaURL(s3FileName);
        media.setVisibility(true);

        mediaRepository.save(media);
    }

    private String buildS3Url(String fileName) {
        if (fileName == null || fileName.startsWith("http")) {
            return fileName;
        }
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
    }

    private boolean isValidImageFile(MultipartFile file) {
        String fileName = file.getOriginalFilename().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpeg");
    }

    private String getFileType(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
            return "image";
        } else if (fileName.endsWith(".mp4") || fileName.endsWith(".avi") || fileName.endsWith(".mov")) {
            return "video";
        }
        return "unknown";
    }
}
