//package com.example.focus;
//
//import com.example.focus.DTO.BookSpaceDTO;
//import com.example.focus.Model.BookSpace;
//import com.example.focus.Repository.BookSpaceRepository;
//import com.example.focus.Repository.ShiftRepository;
//import com.example.focus.Repository.SpaceRepository;
//import com.example.focus.Repository.PhotographerRepository;
//import com.example.focus.Service.BookSpaceService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class BookSpaceServiceTest {
//
//    @InjectMocks
//    private BookSpaceService bookSpaceService;
//
//    @Mock
//    private BookSpaceRepository bookSpaceRepository;
//
//    @Mock
//    private ShiftRepository shiftRepository;
//
//    @Mock
//    private SpaceRepository spaceRepository;
//
//    @Mock
//    private PhotographerRepository photographerRepository;
//
//    private BookSpace bookSpace;
//    private BookSpaceDTO bookSpaceDTO;
//    private List<BookSpace> bookSpaces;
//
//    @BeforeEach
//    void setUp() {
//        bookSpace = new BookSpace(1, 1, null, null, 100.0, "booked");
//        bookSpaceDTO = new BookSpaceDTO(
//                1, 1, "Day Shift", "2025-01-06", "08:00", "16:00", 2, "Space 1", 3, "Photographer A", 100.0, "Confirmed"
//        );
//        bookSpaces = new ArrayList<>();
//        bookSpaces.add(bookSpace);
//    }
//
//    @Test
//    void testGetAllBookings() {
//
//        when(bookSpaceRepository.findAll()).thenReturn(bookSpaces);
//
//        List<BookSpaceDTO> result = bookSpaceService.getAllBookings();
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(bookSpaceDTO, result.get(0));  // التأكد من أن الكائن المحول هو نفس الـ DTO
//        verify(bookSpaceRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetBookingById() {
//
//        when(bookSpaceRepository.findById(1)).thenReturn(Optional.of(bookSpace));
//
//        BookSpaceDTO result = bookSpaceService.getBookingById(1);
//
//        assertNotNull(result);
//        assertEquals(bookSpaceDTO.getId(), result.getId());
//        assertEquals(bookSpaceDTO.getPhotographerName(), result.getPhotographerName());
//        assertEquals(bookSpaceDTO.getStatus(), result.getStatus());
//        verify(bookSpaceRepository, times(1)).findById(1);
//    }
//
//    @Test
//    void testGetBookingById_NotFound() {
//
//        when(bookSpaceRepository.findById(1)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            bookSpaceService.getBookingById(1);
//        });
//
//        assertEquals("Booking not found", exception.getMessage());
//        verify(bookSpaceRepository, times(1)).findById(1);
//    }
//
//    @Test
//    void testPhotographerHistory() {
//        // إعداد البيانات المحاكاة
//        when(bookSpaceRepository.findByPhotographerId(3)).thenReturn(bookSpaces);
//
//        // استدعاء الخدمة
//        List<BookSpaceDTO> result = bookSpaceService.photographerHistory(3);
//
//        // التحقق من النتيجة
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(bookSpaceDTO, result.get(0));
//        verify(bookSpaceRepository, times(1)).findByPhotographerId(3);
//    }
//
//    // اختبارات أخرى مثل إضافة وحذف الحجز يمكن إضافتها بناءً على الوظائف المتاحة في الخدمة
//}
