package com.example.focus.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.Set;
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Studio {
    @Id
    private Integer id;

    @Column(columnDefinition = "varchar(30) not null unique")
    private String name;

    @NotEmpty(message = "Please enter your phone number")
    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with 05 and have 10 digits")
    @Column(columnDefinition = "varchar(10) not null unique")
    private String phoneNumber;

    @NotEmpty(message = "City cannot be empty")
    @Column(columnDefinition = "varchar(30) not null")
    private String city;

    @NotEmpty(message = "Please enter studio Address")
    @Column(columnDefinition = "varchar(60) not null")
    private String address;

    @NotEmpty(message = "commercialRecord")
    @Pattern(regexp = "^7\\d{9}$", message = "commercialRecord must start with 7 and be followed by 9 digits")
    @Column(columnDefinition = "varchar(10) not null unique")
    private String commercialRecord;

    @Pattern(regexp = "active|Inactive|rejected")
    private String status;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @JsonIgnore
    private MyUser myUser;

    @Column(columnDefinition = "varchar(255)")
    private String imageURL;

//    @OneToOne(mappedBy = "studio", cascade = CascadeType.ALL)
//    private ProfileStudio profile;

    @OneToMany
    private Set<Space> spaces;
}
