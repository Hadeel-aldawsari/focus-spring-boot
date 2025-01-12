package com.example.focus.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferEditingInputDTO {

    @NotNull(message = "Request ID cannot be null")
    private Integer requestId;

    @NotNull(message = "Offered price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Offered price must be greater than 0")
    private BigDecimal offeredPrice;

    @NotNull(message = "Estimated completion time cannot be null")
    private LocalDateTime estimatedCompletionTime;
//
//    @NotBlank(message = "Status cannot be blank")
//    @Pattern(
//            regexp = "Applied|Accepted|Rejected",
//            message = "Status must be one of the following: Applied, Accepted, Rejected"
//    )
//    private String status; // Applied, Accepted, Rejected
}
