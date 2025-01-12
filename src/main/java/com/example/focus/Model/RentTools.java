package com.example.focus.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class RentTools {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @FutureOrPresent
    @NotNull(message = "Please enter start date")
    @Column(columnDefinition = "DATETIME not null")
    private LocalDate startDate;

    @Future
    @NotNull(message = "Please enter end date")
    @Column(columnDefinition = "DATETIME not null")
    private LocalDate endDate;


    @Column(columnDefinition = "double ")
    private Double rentPrice;

    @ManyToOne
    @JoinColumn(name = "tool_id")
    private Tool tool;  // The tool being rented
    //
    @ManyToOne
    @JoinColumn(name = "renter_id")
    private Photographer renter;  // The photographer renting the tool
    //
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Photographer owner;  // The photographer who owns the tool

}
