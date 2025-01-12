package com.example.focus.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MediaDTO {

    private String mediaType;
    private String mediaUrl;
    private LocalDateTime uploadDate;

}