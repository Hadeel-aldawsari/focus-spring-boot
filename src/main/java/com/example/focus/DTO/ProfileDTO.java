package com.example.focus.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileDTO {

    private String description;

    private Integer numberOfPosts;

    private String image;
}