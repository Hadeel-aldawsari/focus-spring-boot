package com.example.focus.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProfilePhotographer {
    @Id
    private Integer id;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @PositiveOrZero(message = "Number of posts cannot be negative")
    private Integer numberOfPosts;

    private String image;  // URL of the image

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @JsonIgnore
    private MyUser myUser;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "ProfilePhotographer")
    private Set<Media> medias;

}