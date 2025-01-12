package com.example.focus.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpaceDTO {
    private String name;
    private String type;
    private Integer width;
    private Integer Length;
    private String description;
    private Double priceDay;
    private Double priceNight;
    private Double priceFullDay;
    private String status;
    private String image;
}