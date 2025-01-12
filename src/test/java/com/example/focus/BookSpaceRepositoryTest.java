//package com.example.focus;
//
//import com.example.focus.Model.BookSpace;
//import com.example.focus.Repository.BookSpaceRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.List;
//
//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class BookSpaceRepositoryTest {
//
//    @Autowired
//    BookSpaceRepository bookSpaceRepository;
//
//    BookSpace bookSpace1, bookSpace2;
//
//    @BeforeEach
//    void setUp() {
//        bookSpace1 = new BookSpace();
//        bookSpace1.setStatus("Pending");
//        bookSpace1.setBookingPrice(100.0); // Set a valid price
//
//        bookSpace2 = new BookSpace();
//        bookSpace2.setStatus("Confirmed");
//        bookSpace2.setBookingPrice(200.0); // Set a valid price
//
//        bookSpaceRepository.save(bookSpace1);
//        bookSpaceRepository.save(bookSpace2);
//    }
//
//    @Test
//    public void findByStatusTest() {
//        List<BookSpace> pendingBookings = bookSpaceRepository.findByStatus("Pending");
//        Assertions.assertThat(pendingBookings).hasSize(1);
//        Assertions.assertThat(pendingBookings.get(0).getBookingPrice()).isEqualTo(100.0);
//    }
//}
