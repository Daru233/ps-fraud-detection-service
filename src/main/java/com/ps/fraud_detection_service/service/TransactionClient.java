package com.ps.fraud_detection_service.service;

import com.ps.fraud_detection_service.dto.TransactionStatus;
import com.ps.fraud_detection_service.dto.TransactionStatusPatchRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class TransactionClient {

    private final WebClient webClient;
    private static final String BASE = "\"http://transaction-service\"";
    private static final String URI = "/transactions/{id}/status";

    public TransactionClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(BASE).build(); // replace with real base URL
    }

    public void updateTransactionStatus(UUID transactionId, TransactionStatus status) {
        webClient.patch()
                .uri(URI, transactionId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new TransactionStatusPatchRequest(status))
                .retrieve()
                .toBodilessEntity()
                .block(); // or use async if needed
    }
}
