package com.example.focus.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhotographerDTO {
    private String name;
    private String username;
    private String email;
    private String city;
    private String phoneNumber;
}