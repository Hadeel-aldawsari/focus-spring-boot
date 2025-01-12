package com.example.focus.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    @Column(columnDefinition = "datetime not null")
    private LocalDateTime date;

    @Column(columnDefinition = "datetime not null")
    private LocalDateTime startTime;

    @Column(columnDefinition = "datetime not null")
    private LocalDateTime endTime;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "space_id")
    private Space space;

    @Column(columnDefinition = "varchar(20) not null")
    private String status;
}
