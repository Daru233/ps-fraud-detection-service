package com.ps.fraud_detection_service.dto;

import com.ps.fraud_detection_service.model.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionCreatedEvent(
        @NotNull UUID transactionId,
        @NotNull UUID userId,
        @NotNull BigDecimal amount,
        @NotNull Currency currency,
        @NotBlank String recipientAccount,
        @NotNull Instant createdAt
) {}
