package com.example.focus.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftDTOIn {
    @NotNull(message = "Please provide shift name")
    private String name;

    @NotNull(message = "Start time must be provided")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time must be provided")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @NotNull(message = "Please provide space ID")
    @Positive
    private Integer spaceId;

    @NotEmpty(message = "Shift status is required")
    private String status;
}
