package com.example.focus.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolDTOIn {

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, max = 100, message = "Name should be between 3 and 100 characters")
    @Column(columnDefinition = "varchar(30) not null")
    private String name;

    @NotEmpty(message = "Description cannot be empty")
    @Size(max = 500, message = "Description should not exceed 500 characters")
    @Column(columnDefinition = "varchar(60) not null")
    private String description;

    @NotEmpty(message = "Model number is empty")
    @Column(columnDefinition = "varchar(255) not null")
    private String modelNumber;

    @NotEmpty(message = "Category cannot be empty")
    @Pattern(regexp = "^(Camera|Lens|Tripods|Lighting|Photography Equipment)$", message = "Category must be one of the following: Camera, Lens, Tripods, Lighting, Photography Equipment")
    @Column(columnDefinition = "varchar(23) not null")
    private String category;

    @NotEmpty(message = "Brand cannot be empty")
    @Column(columnDefinition = "varchar(10) not null")
    private String brand;

    @DecimalMin(value = "0.0", inclusive = false, message = "Rental price must be greater than 0")
    @Column(columnDefinition = "decimal not null")
    private Double rentalPrice;


}
