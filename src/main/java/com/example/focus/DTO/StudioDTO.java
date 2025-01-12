package com.example.focus.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudioDTO {
    private String name;
    private String username;
    private String email;
    private String phoneNumber;
    private String city;
    private String address;
    private String commercialRecord;
    private String status;
    private String imageURL;

}
