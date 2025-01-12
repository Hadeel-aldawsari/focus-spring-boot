package com.example.focus.DTO;

import com.example.focus.Model.Photographer;
import com.example.focus.Model.Tool;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentToolsDTOIn {
    @NotNull(message = "Please enter start date")
    @Column(columnDefinition = "DATETIME not null")
    private LocalDate startDate;

    @NotNull(message = "Please enter end date")
    @Column(columnDefinition = "DATETIME not null")
    private LocalDate endDate;

    @Column(columnDefinition = "double ")
    private Double rentPrice;

//    @ManyToOne
//    @JoinColumn(name = "tool_id")
//    private Tool tool;  // The tool being rented
//
//    @ManyToOne
//    @JoinColumn(name = "renter_id")
//    private Photographer renter;  // The photographer renting the tool
//
//    @ManyToOne
//    @JoinColumn(name = "owner_id")
//    private Photographer owner;  // The photographer who owns the tool
}
