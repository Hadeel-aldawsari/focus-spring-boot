package com.example.focus.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(20) not null")
    private String name;

    @Column(columnDefinition = "varchar(20) not null")
    private String type;

//    @Column(columnDefinition = "double not null")
//    private Double area;

   @Column(columnDefinition = "double not null")
    private Integer width;

    @Column(columnDefinition = "double not null")
    private Integer Length;


    @Column(columnDefinition = "varchar(150) not null")
    private String description;

    @Column(columnDefinition = "double not null")
    private Double dayPrice;
    @Column(columnDefinition = "double not null")
    private Double nightPrice;
    @Column(columnDefinition = "double not null")
    private Double fullDayPrice;

    @Column
    private String status;

    @Column(nullable = false, columnDefinition = "varchar(255) not null")
    private String image;

    @ManyToOne
    @JoinColumn(name = "studio_id")
    private Studio studio;

    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Shift> shifts;


    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookSpace> bookings;

}
