package com.example.focus.DTO;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTOin{

    private String image;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

}
