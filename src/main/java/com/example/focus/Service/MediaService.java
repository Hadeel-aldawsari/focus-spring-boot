package com.example.focus.Service;

import com.example.focus.DTO.MediaDTO;
import com.example.focus.Model.Media;
import com.example.focus.Repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final MediaRepository mediaRepository;

    // إرجاع عدد الفيديوهات لمصور معين
    public Integer getVideoCountByPhotographer(Integer photographerId) {
        return mediaRepository.countByProfilePhotographerIdAndMediaType(photographerId, "video");
    }

    // إرجاع عدد الصور لمصور معين
    public Integer getImageCountByPhotographer(Integer photographerId) {
        return mediaRepository.countByProfilePhotographerIdAndMediaType(photographerId, "image");
    }

    // إرجاع الفيديوهات فقط لمصور معين
    public List<MediaDTO> getVideosByPhotographer(Integer photographerId) {
        List<Media> mediaList = mediaRepository.findByProfilePhotographerIdAndMediaType(photographerId, "video");
        List<MediaDTO> mediaDTOList = new ArrayList<>();

        //  for loop لتحويل Media إلى MediaDTO
        for (Media media : mediaList) {
            MediaDTO mediaDTO = new MediaDTO(
                    media.getMediaType(),
                    media.getMediaURL(),
                  media.getUploadDate()
            );
            mediaDTOList.add(mediaDTO);
        }

        return mediaDTOList;
    }

    // إرجاع الصور فقط لمصور معين
    public List<MediaDTO> getImagesByPhotographer(Integer photographerId) {
        List<Media> mediaList = mediaRepository.findByProfilePhotographerIdAndMediaType(photographerId, "image");
        List<MediaDTO> mediaDTOList = new ArrayList<>();

        // استخدام for loop لتحويل Media إلى MediaDTO
        for (Media media : mediaList) {
            MediaDTO mediaDTO = new MediaDTO(
                    media.getMediaType(),
                    media.getMediaURL(),
                    media.getUploadDate()
            );
            mediaDTOList.add(mediaDTO);
        }

        return mediaDTOList;
    }

    //**************************Editor

    // إرجاع عدد الفيديوهات editor معين
    public Integer getVideoCountByEditor(Integer editorId) {
        return mediaRepository.countByProfileEditorIdAndMediaType(editorId, "video");
    }

    // إرجاع عدد الصور editor معين
    public Integer getImageCountByEditor(Integer editorId) {
        return mediaRepository.countByProfileEditorIdAndMediaType(editorId, "image");
    }

    // إرجاع الفيديوهات فقط editor معين
    public List<MediaDTO> getVideosByEditor(Integer editorId) {
        List<Media> mediaList = mediaRepository.findByProfileEditorIdAndMediaType(editorId, "video");
        List<MediaDTO> mediaDTOList = new ArrayList<>();

        // استخدام for loop لتحويل Media إلى MediaDTO
        for (Media media : mediaList) {
            MediaDTO mediaDTO = new MediaDTO(
                    media.getMediaType(),
                    media.getMediaURL(),
                    media.getUploadDate()
            );
            mediaDTOList.add(mediaDTO);
        }

        return mediaDTOList;
    }

    // إرجاع الصور فقط editor معين
    public List<MediaDTO> getImagesByEditor(Integer editorId) {
        List<Media> mediaList = mediaRepository.findByProfileEditorIdAndMediaType(editorId, "image");
        List<MediaDTO> mediaDTOList = new ArrayList<>();

        // استخدام for loop لتحويل Media إلى MediaDTO
        for (Media media : mediaList) {
            MediaDTO mediaDTO = new MediaDTO(
                    media.getMediaType(),
                    media.getMediaURL(),
                    media.getUploadDate()
            );
            mediaDTOList.add(mediaDTO);
        }

        return mediaDTOList;
    }
}