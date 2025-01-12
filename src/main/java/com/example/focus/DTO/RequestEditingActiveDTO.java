package com.example.focus.DTO;

import com.example.focus.Model.Media;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestEditingActiveDTO {

    private LocalDateTime estimatedCompletionDate;
    private String description;
    private String fullCameraName;
    private String sensorSize;
    private String kitLens;
    private String viewFinder;
    private String nativeISO;
    private String editorName;
    private String photographeName;
    private String status;//  Active, Closed, AwaitingOffer
    private List<MediaDTO> media;
}
