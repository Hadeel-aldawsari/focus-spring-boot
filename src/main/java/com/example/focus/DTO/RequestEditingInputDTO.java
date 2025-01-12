package com.example.focus.DTO;

import com.example.focus.Model.Media;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestEditingInputDTO {

    @NotNull(message = "Estimated completion date cannot be null")
    private LocalDateTime estimatedCompletionDate;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Full camera name cannot be blank")
    private String fullCameraName;

    @NotBlank(message = "Sensor size cannot be blank")
    private String sensorSize;

    @NotBlank(message = "Kit lens cannot be blank")
    private String kitLens;

    @NotBlank(message = "View finder cannot be blank")
    private String viewFinder;

    @NotBlank(message = "Native ISO cannot be blank")
    private String nativeISO;

    private Set<Media> mediaList;



//    @NotNull(message = "Editor ID cannot be null")
//    private Integer editorId;

//    @NotBlank(message = "Status cannot be blank")
//    @Pattern(
//            regexp = "Pending|Active|Closed|AwaitingOffer",
//            message = "Status must be one of the following: Pending, Active, Closed, AwaitingOffer"
//    )
//    private String status; // Pending, Active, Closed, AwaitingOffer
}
