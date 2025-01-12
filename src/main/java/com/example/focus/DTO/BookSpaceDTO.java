package com.example.focus.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSpaceDTO {

    private Integer id; // Booking ID
    private Integer shiftId; // Shift ID associated with the booking
    private String shiftName; // Name of the shift (e.g., "Day Shift", "Night Shift", "Full Day")
    private String shiftDate; // Date of the shift
    private String shiftStartTime; // Start time of the shift
    private String shiftEndTime; // End time of the shift
    private Integer spaceId; // Space ID associated with the shift
    private String spaceName; // Name of the space
    private Integer photographerId; // Photographer ID
    private String photographerName; // Photographer's name
    private Double bookingPrice; // Total booking price
    private String status; // Booking status (e.g., "Confirmed", "Cancelled")
}
