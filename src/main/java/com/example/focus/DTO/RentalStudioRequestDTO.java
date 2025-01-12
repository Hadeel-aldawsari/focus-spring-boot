package com.example.focus.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalStudioRequestDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String note;
}
