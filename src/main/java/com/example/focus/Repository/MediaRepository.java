package com.example.focus.Repository;

import com.example.focus.Model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media,Integer> {
    Media findMediaById(Integer id);
//
//    // إرجاع عدد الفيديوهات او الصور لمصور أو محرر معين
@Query("SELECT COUNT(m) FROM Media m WHERE m.ProfilePhotographer.id = :photographerId AND m.mediaType = :mediaType")
Integer countByProfilePhotographerIdAndMediaType(Integer photographerId, String mediaType);

    @Query("SELECT COUNT(m) FROM Media m WHERE m.ProfileEditor.id = :editorId AND m.mediaType = :mediaType")
    Integer countByProfileEditorIdAndMediaType(Integer editorId, String mediaType);


//    // إرجاع الصور او الفيديوهات فقط لمصور أو محرر معين
    @Query("SELECT m FROM Media m WHERE m.ProfilePhotographer.id = :photographerId AND m.mediaType = :mediaType")
    List<Media> findByProfilePhotographerIdAndMediaType(Integer photographerId, String mediaType);

    @Query("SELECT m FROM Media m WHERE m.ProfileEditor.id = :editorId AND m.mediaType = :mediaType")
    List<Media> findByProfileEditorIdAndMediaType(Integer editorId, String mediaType);


}