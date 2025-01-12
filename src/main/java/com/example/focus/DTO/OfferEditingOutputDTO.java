package com.example.focus.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferEditingOutputDTO {

    private Integer requestId;
    private String editorName;
    private LocalDateTime offerDate;
    private BigDecimal offeredPrice;
    private LocalDateTime estimatedCompletionTime;
    private String status; // Applied, Accepted, Rejected
}
