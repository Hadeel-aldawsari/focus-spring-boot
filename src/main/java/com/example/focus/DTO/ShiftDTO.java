package com.example.focus.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftDTO {
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer spaceId;
    private String status;
}
