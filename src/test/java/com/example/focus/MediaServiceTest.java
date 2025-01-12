//package com.example.focus;
//
//import com.example.focus.DTO.MediaDTO;
//import com.example.focus.Model.Media;
//import com.example.focus.Repository.MediaRepository;
//import com.example.focus.Service.MediaService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class MediaServiceTest {
//
//    @InjectMocks
//    private MediaService mediaService;
//
//    @Mock
//    private MediaRepository mediaRepository;
//
//    private Media media1, media2;
//    private List<Media> mediaList;
//
//    @BeforeEach
//    public void setUp() {
//        // إعداد بعض البيانات الاختبارية
//        media1 = new Media(1, "video", LocalDateTime.now(), true, "http://video1.com", null, null, null);
//        media2 = new Media(2, "video", LocalDateTime.now(), true, "http://video2.com", null, null, null);
//        mediaList = Arrays.asList(media1, media2);
//    }
//
//    @Test
//    public void testGetVideoCountByPhotographer() {
//        Integer photographerId = 1;
//
//        when(mediaRepository.countByProfilePhotographerIdAndMediaType(photographerId, "video")).thenReturn(2);
//        Integer count = mediaService.getVideoCountByPhotographer(photographerId);
//        assertEquals(2, count);
//
//        verify(mediaRepository, times(1)).countByProfilePhotographerIdAndMediaType(photographerId, "video");
//    }
//
//    @Test
//    public void testGetImageCountByPhotographer() {
//        Integer photographerId = 1;
//
//        when(mediaRepository.countByProfilePhotographerIdAndMediaType(photographerId, "image")).thenReturn(5);
//
//        Integer count = mediaService.getImageCountByPhotographer(photographerId);
//
//        assertEquals(5, count);
//
//        verify(mediaRepository, times(1)).countByProfilePhotographerIdAndMediaType(photographerId, "image");
//    }
//
//    @Test
//    public void testGetVideosByPhotographer() {
//        Integer photographerId = 1;
//
//        when(mediaRepository.findByProfilePhotographerIdAndMediaType(photographerId, "video")).thenReturn(mediaList);
//
//        List<MediaDTO> mediaDTOList = mediaService.getVideosByPhotographer(photographerId);
//
//        assertEquals(2, mediaDTOList.size());
//        assertEquals("video", mediaDTOList.get(0).getMediaType());
//        assertEquals("http://video1.com", mediaDTOList.get(0).getMediaUrl());
//
//        verify(mediaRepository, times(1)).findByProfilePhotographerIdAndMediaType(photographerId, "video");
//    }
//}
