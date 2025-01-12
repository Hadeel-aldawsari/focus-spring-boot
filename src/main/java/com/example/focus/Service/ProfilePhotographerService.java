package com.example.focus.Service;

import com.example.focus.ApiResponse.ApiException;
import com.example.focus.DTO.PhotographerDTO;
import com.example.focus.DTO.ProfileDTO;
import com.example.focus.DTO.ProfileDTOin;
import com.example.focus.Model.Media;
import com.example.focus.Model.MyUser;
import com.example.focus.Model.ProfileEditor;
import com.example.focus.Model.ProfilePhotographer;
import com.example.focus.Repository.MediaRepository;
import com.example.focus.Repository.MyUserRepository;
import com.example.focus.Repository.ProfileEditorRepository;
import com.example.focus.Repository.ProfilePhotographerRepository;
import com.example.focus.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class ProfilePhotographerService {
    private final ProfilePhotographerRepository profilePhotographerRepository ;
    private final ProfileEditorRepository profileEditorRepository ;
    private final MediaRepository mediaRepository;
    private final MyUserRepository myUserRepository;
    private final com.example.focus.service.S3Service s3Service;

    String bucketName = "my-app-media-bucket";
    String region = "ap-northeast-1";
    String fileName = "profile-picture.jpg";

    public ProfilePhotographerService(ProfilePhotographerRepository profilePhotographerRepository, ProfileEditorRepository profileEditorRepository, MediaRepository mediaRepository, MyUserRepository myUserRepository, S3Service s3Service) {
        this.profilePhotographerRepository = profilePhotographerRepository;
        this.profileEditorRepository = profileEditorRepository;
        this.mediaRepository = mediaRepository;
        this.myUserRepository = myUserRepository;
        this.s3Service = s3Service;
    }

    public List<ProfilePhotographer> getAllProfiles() {
        List<ProfilePhotographer> profiles = profilePhotographerRepository.findAll();

        for (ProfilePhotographer profile : profiles) {
            if (profile.getImage() != null && !profile.getImage().startsWith("http")) {
                String s3Url = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + profile.getImage();
                profile.setImage(s3Url);
            }
        }

        return profiles;
    }



    public ProfilePhotographer getMyProfile(Integer photographerid ) {
        ProfilePhotographer profile = profilePhotographerRepository.findProfilePhotographerById(photographerid);
        if(profile == null) {
            throw new ApiException("Photographer not found");
        }

        if (profile.getImage() != null && !profile.getImage().startsWith("http")) {
            String s3Url = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + profile.getImage();
            profile.setImage(s3Url);
        }

        return profile;
    }


    public ProfilePhotographer getSpecificProfile(Integer userid1, Integer userid2) {
        MyUser user1 = myUserRepository.findMyUserById(userid1);
        if (user1 == null) {
            throw new ApiException("User not found");
        }

        MyUser user2 = myUserRepository.findMyUserById(userid2);
        if (user2 == null) {
            throw new ApiException("User you search about not found");
        }

        ProfilePhotographer profilePhotographer = profilePhotographerRepository.findProfilePhotographerById(userid2);
        if (profilePhotographer != null && profilePhotographer.getImage() != null && !profilePhotographer.getImage().startsWith("http")) {
            String s3Url = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + profilePhotographer.getImage();
            profilePhotographer.setImage(s3Url);
        }

        return profilePhotographer;
    }



    //update profile
    public void updateProfile(Integer id, ProfileDTOin profileDTOin, MultipartFile file) throws IOException {
        MyUser user = myUserRepository.findMyUserById(id);
        if (!isValidImageFile(file)) {
            throw new ApiException("Invalid image file. Only JPG, PNG, and JPEG files are allowed");
        }
        if (user != null) {
             // حفظ اسم الملف في قاعدة البيانات

            String s3FileName = s3Service.uploadFile(file); // رفع الملف إلى S3
            user.getProfilePhotographer().setDescription(profileDTOin.getDescription());
            user.getProfilePhotographer().setImage(s3FileName);
            myUserRepository.save(user);
        } else {
            throw new ApiException("Profile Not Found");
        }
    }



    public void deleteProfile(Integer id) {
        ProfilePhotographer existingProfile = profilePhotographerRepository.findProfilePhotographerById(id);
        if (existingProfile != null) {
            profilePhotographerRepository.delete(existingProfile);
        } else {
            throw new ApiException("Profile Not Found");
        }
    }





    public void uploadMedia(Integer profileId, MultipartFile file) throws IOException {

        ProfilePhotographer profilePhotographer = profilePhotographerRepository.findProfilePhotographerById(profileId);
        if (profilePhotographer == null) {
            throw new ApiException("ProfilePhotographer not found");
        }


        String fileName = file.getOriginalFilename();
        String fileType = getFileType(fileName);

        if ("unknown".equals(fileType)) {
            throw new ApiException("Unsupported file type. Only images and videos are allowed");
        }

        // رفع الملف إلى S3
        String s3FileName = s3Service.uploadFile(file);


        profilePhotographer.setNumberOfPosts(profilePhotographer.getNumberOfPosts() + 1);

        Media media = new Media();
        media.setProfilePhotographer(profilePhotographer);
        media.setMediaType(fileType);
        media.setUploadDate(LocalDateTime.now());
        media.setMediaURL(s3FileName); //
        media.setVisibility(true);

        mediaRepository.save(media);
    }





     //التحقق من نوع الملف
    private boolean isValidImageFile(MultipartFile file) {
        String fileName = file.getOriginalFilename().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpeg");
    }

    // حفظ الصورة في المسار المحدد
//    private String saveImageFile(MultipartFile file) throws IOException {
//        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
//        Path path = Paths.get(UPLOAD_PROFILE_DIR + fileName);
//        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//        return fileName;
//    }























    private String getFileType(String fileName) {
        String fileType = "unknown";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
            fileType = "image";
        } else if (fileName.endsWith(".mp4") || fileName.endsWith(".avi") || fileName.endsWith(".mov")) {
            fileType = "video";
        }
        return fileType;
    }




}