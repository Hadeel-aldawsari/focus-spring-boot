package com.example.focus.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSpaceDTOIn {

    @NotNull(message = "Shift name cannot be null")
    private String shiftName; // Name of the shift to book (e.g., "Day Shift", "Night Shift", "Full Day")

    @FutureOrPresent(message = "date must be present or future")
    @NotNull(message = "Date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date; // Date of the shift

    @NotNull(message = "Space ID cannot be null")
    private Integer spaceId; // ID of the space being booked


    // ID of the photographer booking the shift
}
