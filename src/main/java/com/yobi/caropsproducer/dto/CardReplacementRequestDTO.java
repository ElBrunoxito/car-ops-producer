package com.yobi.caropsproducer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardReplacementRequestDTO {
    @NotBlank private String requestId;
    @NotBlank private String customerId;
    @NotBlank private String cardPanMasked; // 4111********1111
    @NotBlank private String reasonCode;    // LOST/DAMAGED/UPGRADE
    @NotBlank private String priority;      // HIGH/NORMAL
    @NotBlank private String branchCode;
    @NotBlank private String deliveryAddress;
    @Builder.Default private Instant requestedAt = Instant.now();
    @NotBlank private String correlationId;
    @NotBlank private String status;        // REQUESTED/VALIDATED/...
}