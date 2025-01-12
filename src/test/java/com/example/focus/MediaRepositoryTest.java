//package com.example.focus;
//
//import com.example.focus.Model.Media;
//import com.example.focus.Model.ProfileEditor;
//import com.example.focus.Model.ProfilePhotographer;
//import com.example.focus.Repository.MediaRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class MediaRepositoryTest {
//
//    @Autowired
//    private MediaRepository mediaRepository;
//
//    private Media media1, media2, media3;
//    private ProfilePhotographer photographer;
//    private ProfileEditor editor;
//
//    @BeforeEach
//    void setUp() {
//
//        photographer = new ProfilePhotographer();
//        photographer.setId(1);
//
//        editor = new ProfileEditor();
//        editor.setId(1);
//
//
//        media1 = new Media(null, "image", LocalDateTime.now(), true, "http://image1.url", photographer, null, null);
//        media2 = new Media(null, "video", LocalDateTime.now(), true, "http://video1.url", photographer, null, null);
//        media3 = new Media(null, "image", LocalDateTime.now(), false, "http://image2.url", null, null, editor);
//
//
//        mediaRepository.save(media1);
//        mediaRepository.save(media2);
//        mediaRepository.save(media3);
//    }
//
//    @Test
//    void testFindMediaById() {
//        Media result = mediaRepository.findMediaById(media1.getId());
//        Assertions.assertThat(result).isNotNull();
//        Assertions.assertThat(result.getId()).isEqualTo(media1.getId());
//    }
//
//    @Test
//    void testCountByProfilePhotographerIdAndMediaType() {
//        Integer count = mediaRepository.countByProfilePhotographerIdAndMediaType(1, "image");
//        Assertions.assertThat(count).isEqualTo(1); // 1 image for photographer id 1
//    }
//
//    @Test
//    void testCountByProfileEditorIdAndMediaType() {
//        Integer count = mediaRepository.countByProfileEditorIdAndMediaType(1, "image");
//        Assertions.assertThat(count).isEqualTo(1); // 1 image for editor id 1
//    }
//
//    @Test
//    void testFindByProfilePhotographerIdAndMediaType() {
//        List<Media> result = mediaRepository.findByProfilePhotographerIdAndMediaType(1, "image");
//        Assertions.assertThat(result).hasSize(1); // 1 image for photographer id 1
//        Assertions.assertThat(result.get(0).getMediaType()).isEqualTo("image");
//    }
//
//    @Test
//    void testFindByProfileEditorIdAndMediaType() {
//        List<Media> result = mediaRepository.findByProfileEditorIdAndMediaType(1, "image");
//        Assertions.assertThat(result).hasSize(1); // 1 image for editor id 1
//        Assertions.assertThat(result.get(0).getMediaType()).isEqualTo("image");
//    }
//}
