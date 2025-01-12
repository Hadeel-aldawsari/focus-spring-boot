package com.example.focus.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpaceDTOIn {
    @NotEmpty(message = "Please enter space name")
    private String name;
    @NotEmpty(message = "Please enter space type")
    private String type;
//    @NotNull(message = "Please enter space area")
//    @Positive
//    private Double area;

    @Column(columnDefinition = "double not null")
    @Min(value = 1,message = "width should be 1 at least")
    private Integer width;

    @Min(value = 1,message = "width should be 1 at least")
    @Column(columnDefinition = "double not null")
    private Integer Length;

    private String description;
    @NotNull(message = "Please enter space price")
    @PositiveOrZero(message = "price can be zero or more")
    private Double dayPrice;
    @NotNull(message = "Please enter space price")
    @PositiveOrZero(message = "price can be zero or more")
    private Double nightPrice;
    @NotNull(message = "Please enter space price")
    @PositiveOrZero(message = "price can be zero or more")
    private Double fullDayPrice;

//    @Column(columnDefinition = "varchar(255) not null")
//    @NotEmpty(message = "Image file cannot be empty")
//    private MultipartFile image;

  //  private Integer studioID;
}
