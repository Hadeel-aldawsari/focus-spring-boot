package com.example.focus.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ToolDTO {

    private String name;

    private String description;

    private String category;

    private String modelNumber;

    private Integer numberOfRentals;

    private String brand;

    private Double rentalPrice;

    private String imageUrl;

}