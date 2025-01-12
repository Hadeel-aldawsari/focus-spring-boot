package com.example.focus.Service;

import com.example.focus.ApiResponse.ApiException;
import com.example.focus.DTO.BookSpaceDTO;
import com.example.focus.DTO.BookSpaceDTOIn;
import com.example.focus.Model.BookSpace;
import com.example.focus.Model.MyUser;
import com.example.focus.Model.Shift;
import com.example.focus.Model.Studio;
import com.example.focus.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSpaceService {

    private final BookSpaceRepository bookSpaceRepository;
    private final ShiftRepository shiftRepository;
    private final SpaceRepository spaceRepository;
    private final MyUserRepository myUserRepository;
    private final EmailService emailService;
    private final PhotographerRepository photographerRepository;
    private final StudioRepository studioRepository;

    public List<BookSpaceDTO> getAllBookings(Integer userid) {
        MyUser user=myUserRepository.findMyUserById(userid);
        if(user==null)throw new ApiException("user not found");

        List<BookSpace> bookings = bookSpaceRepository.findAll();
        List<BookSpaceDTO> bookingDTOs = new ArrayList<>();
        for (BookSpace booking : bookings) {
            bookingDTOs.add(convertToDTO(booking));
        }
        return bookingDTOs;
    }

    public BookSpaceDTO photographerGetBookingById(Integer userid,Integer bookid) {
        BookSpace booking = bookSpaceRepository.findById(bookid).orElse(null);
        MyUser user=myUserRepository.findMyUserById(userid);
        if(user==null)throw new ApiException("user not found");

        if(! booking.getPhotographerId().equals(userid)){
            throw new ApiException("unauthenticated, you don't own this book");
        }

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }
        return convertToDTO(booking);
    }

    public List<BookSpaceDTO> photographerHistory(Integer photographerId) {
        List<BookSpace> bookings = bookSpaceRepository.findByPhotographerId(photographerId);
        List<BookSpaceDTO> bookingDTOs = new ArrayList<>();
        for (BookSpace booking : bookings) {
            bookingDTOs.add(convertToDTO(booking));
        }
        return bookingDTOs;
    }

    public BookSpaceDTO createSpaceBooking(Integer userid,BookSpaceDTOIn bookingDTOIn) {
        MyUser user=myUserRepository.findMyUserById(userid);
        if(user==null)throw new ApiException("user not found");

        Shift fullDayShift = shiftRepository.findShiftBySpaceIdAndDateAndName(
                bookingDTOIn.getSpaceId(), bookingDTOIn.getDate().atStartOfDay(), "Full Day");

        Shift shift = shiftRepository.findShiftBySpaceIdAndDateAndName(bookingDTOIn.getSpaceId(),
                bookingDTOIn.getDate().atStartOfDay(), bookingDTOIn.getShiftName());
        BookSpace oldBook = bookSpaceRepository.findBookByShiftId(shift.getId());

        if (oldBook != null) {
            throw new ApiException("This shift is already booked");
        }
        if (shift.getStatus().equals("Unavailable")) {
            throw new ApiException("This shift is not available");
        }

        if ("Night Shift".equals(shift.getName()) || "Morning Shift".equals(shift.getName())) {
            fullDayShift.setStatus("Unavailable");
            shiftRepository.save(fullDayShift);
        }

        if ("Full Day".equals(shift.getName())) {
            Shift dayShift = shiftRepository.findShiftBySpaceIdAndDateAndName(
                    bookingDTOIn.getSpaceId(), bookingDTOIn.getDate().atStartOfDay(), "Morning Shift");
            Shift nightShift = shiftRepository.findShiftBySpaceIdAndDateAndName(
                    bookingDTOIn.getSpaceId(), bookingDTOIn.getDate().atStartOfDay(), "Night Shift");
            dayShift.setStatus("Unavailable");
            nightShift.setStatus("Unavailable");
            shiftRepository.save(dayShift);
            shiftRepository.save(nightShift);
        }

        shift.setStatus("Unavailable");
        shiftRepository.save(shift);

        BookSpace booking = createBookingEntity(bookingDTOIn, shift);
        BookSpace savedBooking = bookSpaceRepository.save(booking);

        // Send email notifications
        sendBookingEmails(savedBooking);

        return convertToDTO(savedBooking);
    }

    public BookSpaceDTO updateBookingStatus(Integer id, Integer bookingid,String status) {
        BookSpace booking = bookSpaceRepository.findById(id).orElse(null);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }

        Studio studio=studioRepository.findStudioById(bookingid);
        if (! studio.getId().equals(booking.getSpace().getStudio().getId())) {
            throw new ApiException("unauthenticated, you don't own this book");
        }

        booking.setStatus(status);

        if ("Cancelled".equals(status)) {
            Shift shift = booking.getShift();
            shift.setStatus("Available");
            shiftRepository.save(shift);

            // Send cancellation emails
            sendCancellationEmails(booking);
        }
        return convertToDTO(bookSpaceRepository.save(booking));
    }

    public void deleteBooking(Integer id) {
        BookSpace booking = bookSpaceRepository.findById(id).orElse(null);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }
        Shift shift = booking.getShift();
        if (shift != null) {
            shift.setStatus("Available");
            shiftRepository.save(shift);
        }

        // Send deletion emails
        sendDeletionEmails(booking);

        bookSpaceRepository.delete(booking);
    }

    public BookSpaceDTO acceptBooking(Integer userid,Integer bookingId) {
        BookSpace booking = bookSpaceRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }

        MyUser user=myUserRepository.findMyUserById(userid);
        if(user==null)throw new ApiException("user not found");

        Studio studio=studioRepository.findStudioById(bookingId);
        if (! studio.getId().equals(booking.getSpace().getStudio().getId())) {
            throw new ApiException("unauthenticated, you don't own this book");
        }

        booking.setStatus("Confirmed");
        bookSpaceRepository.save(booking);

        // Send confirmation emails
        sendConfirmationEmails(booking);

        return convertToDTO(booking);
    }

    public void CancelBooking(Integer userid,Integer bookingid) {
        BookSpace booking = bookSpaceRepository.findById(bookingid).orElse(null);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }
        Studio studio=studioRepository.findStudioById(userid);
        if (! studio.getId().equals(booking.getSpace().getStudio().getId())) {
            throw new ApiException("unauthenticated, you don't own this book");
        }
        Shift shift = booking.getShift();
        if (shift == null) {
            throw new IllegalStateException("Shift not found for the booking");
        }

        Shift fullDayShift = shiftRepository.findShiftBySpaceIdAndDateAndName(
                booking.getSpace().getId(),
                shift.getDate(),
                "Full Day");

        if ("Full Day".equalsIgnoreCase(shift.getName())) {
            if (fullDayShift != null) {
                fullDayShift.setStatus("Available");
                shiftRepository.save(fullDayShift);
            }
        } else {
            shift.setStatus("Available");
            shiftRepository.save(shift);
        }

        booking.setStatus("Cancelled");
        bookSpaceRepository.save(booking);

        // Send cancellation emails
        sendCancellationEmails(booking);
    }


    //without end point
    private void sendBookingEmails(BookSpace booking) {
        String photographerEmail = photographerRepository.findPhotographersById(booking.getPhotographerId())
                .getMyUser().getEmail();
        String studioOwnerEmail = booking.getSpace().getStudio().getMyUser().getEmail();

        String photographerSubject = "Booking Confirmed";
        String photographerBody = "Dear Photographer,\n\nYour booking for " + booking.getSpace().getName() +
                " on " + booking.getShift().getDate() + " during the " + booking.getShift().getName() +
                " has been successfully created.\n\nThank you!";

        String studioOwnerSubject = "New Booking Notification";
        String studioOwnerBody = "Dear Studio Owner,\n\nA new booking has been made for your space " +
                booking.getSpace().getName() + " on " + booking.getShift().getDate() + " during the " +
                booking.getShift().getName() + ".\n\nPlease log in to view the details.";

        emailService.sendEmail(photographerEmail, photographerSubject, photographerBody);
        emailService.sendEmail(studioOwnerEmail, studioOwnerSubject, studioOwnerBody);
    }

    private void sendCancellationEmails(BookSpace booking) {
        String photographerEmail = photographerRepository.findPhotographersById(booking.getPhotographerId())
                .getMyUser().getEmail();
        String studioOwnerEmail = booking.getSpace().getStudio().getMyUser().getEmail();

        String photographerSubject = "Booking Cancelled";
        String photographerBody = "Dear Photographer,\n\nYour booking for " + booking.getSpace().getName() +
                " on " + booking.getShift().getDate() + " has been cancelled.\n\nThank you!";

        String studioOwnerSubject = "Booking Cancelled";
        String studioOwnerBody = "Dear Studio Owner,\n\nThe booking for your space " +
                booking.getSpace().getName() + " on " + booking.getShift().getDate() +
                " during the " + booking.getShift().getName() + " has been cancelled.\n\nThank you!";

        emailService.sendEmail(photographerEmail, photographerSubject, photographerBody);
        emailService.sendEmail(studioOwnerEmail, studioOwnerSubject, studioOwnerBody);
    }

    private void sendDeletionEmails(BookSpace booking) {
        sendCancellationEmails(booking); // Same content as cancellation emails
    }

    private void sendConfirmationEmails(BookSpace booking) {
        sendBookingEmails(booking); // Reuse booking emails for confirmation
    }

    private BookSpace createBookingEntity(BookSpaceDTOIn bookingDTOIn, Shift shift) {
        BookSpace booking = new BookSpace();
//        booking.setPhotographerId(bookingDTOIn.getPhotographerId());
        booking.setShift(shift);
        booking.setStatus("Pending");
        booking.setBookingPrice(shift.getPrice());
        booking.setSpace(shift.getSpace());
        return booking;
    }

    private BookSpaceDTO convertToDTO(BookSpace booking) {
        BookSpaceDTO dto = new BookSpaceDTO();
        dto.setId(booking.getId());
        dto.setShiftId(booking.getShift().getId());
        dto.setPhotographerId(booking.getPhotographerId());
        dto.setBookingPrice(booking.getBookingPrice());
        dto.setStatus(booking.getStatus());
        dto.setShiftName(booking.getShift().getName());
        dto.setShiftDate(booking.getShift().getDate().toString());
        dto.setShiftStartTime(booking.getShift().getStartTime().toLocalTime().toString());
        dto.setShiftEndTime(booking.getShift().getEndTime().toLocalTime().toString());
        dto.setSpaceId(booking.getShift().getSpace().getId());
        dto.setSpaceName(booking.getShift().getSpace().getName());
        return dto;
    }
}
