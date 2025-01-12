package com.example.focus.Controller;

import com.example.focus.ApiResponse.ApiResponse;
import com.example.focus.DTO.BookSpaceDTO;
import com.example.focus.DTO.BookSpaceDTOIn;
import com.example.focus.Model.MyUser;
import com.example.focus.Service.BookSpaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/focus/book-space")
@RequiredArgsConstructor
public class BookSpaceController {

    private final BookSpaceService bookSpaceService;

    //Studio
    @GetMapping("/get-all")
    public ResponseEntity getAllBookings(@AuthenticationPrincipal MyUser myUser) {
        List<BookSpaceDTO> bookings = bookSpaceService.getAllBookings(myUser.getId());
        return ResponseEntity.status(200).body(bookings);
    }

    //photographer
    @GetMapping("/photographer/get-by-id/{bookingid}")
    public ResponseEntity photographerGetBookingById(@AuthenticationPrincipal MyUser myuser,@PathVariable Integer bookingid) {
        BookSpaceDTO booking = bookSpaceService.photographerGetBookingById(myuser.getId(),bookingid);
        return ResponseEntity.status(200).body(booking);
    }

    //photographer
    @GetMapping("/get-photographer-history")
    public ResponseEntity getPhotographerHistory(@AuthenticationPrincipal MyUser myUser ) {
        List<BookSpaceDTO> bookings = bookSpaceService.photographerHistory(myUser.getId());
        return ResponseEntity.status(200).body(bookings);
    }
//photographer
    @PostMapping("/create-space-booking")
    public ResponseEntity createSpaceBooking(@AuthenticationPrincipal MyUser myUser,@RequestBody @Valid BookSpaceDTOIn bookingDTOIn) {
        BookSpaceDTO booking = bookSpaceService.createSpaceBooking(myUser.getId(),bookingDTOIn);
        return ResponseEntity.status(200).body(booking);
    }

    //studio
    @PutMapping("/cancel-booking/{bookingid}")
    public ResponseEntity CancelBooking(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer bookingid) {
        bookSpaceService.CancelBooking(myUser.getId(),bookingid);
        return ResponseEntity.status(200).body(new ApiResponse("Booking Canceled successfully"));
    }

    //studio
    @PutMapping("/update-booking-status/{bookingid}/{status}")
    public ResponseEntity updateBookingStatus(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer bookingid,@PathVariable String status) {
        BookSpaceDTO updatedBooking = bookSpaceService.updateBookingStatus(myUser.getId(),bookingid,status);
        return ResponseEntity.status(200).body(updatedBooking);
    }

    //studio
    @PutMapping("/accept-booking/{bookingId}")
    public ResponseEntity acceptBooking( @AuthenticationPrincipal MyUser myUser,@PathVariable Integer bookingId) {
        bookSpaceService.acceptBooking(myUser.getId(), bookingId);
        return ResponseEntity.status(200).body(new ApiResponse("Booking accepted successfully"));
    }
}
