package com.ps.fraud_detection_service.dto;

import jakarta.validation.constraints.NotNull;

public record TransactionStatusPatchRequest(
        @NotNull TransactionStatus status
) {}
