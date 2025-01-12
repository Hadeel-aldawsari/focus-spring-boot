package com.example.focus.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProfileStudio {
    @Id
    private Integer id;

    @NotEmpty(message = "Please enter description")
    @Column(columnDefinition = "varchar(60) not null")
    private String description;

    @NotEmpty(message = "Please enter studio location")
    @Column(columnDefinition = "varchar(20) not null")
    private String location;

    @NotEmpty(message = "Please enter studio commercial Record")
    @Pattern(regexp = "10\\d{9}",message = "Enter commercial Record start with 10 and just 9 digits")
    @Column(columnDefinition = "varchar(9) not null unique")
    private String commercialRecord;

    @Column(columnDefinition = "boolean ")
    private Boolean isActivated;

    @OneToOne
    @JoinColumn(name = "studio_id", referencedColumnName = "id")
    private Studio studio;


}