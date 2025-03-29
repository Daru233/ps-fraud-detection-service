package com.ps.fraud_detection_service.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fraud")
public record FraudProperties(
        String cron,
        String zone,
        String subscription,
        int maxMessages,
        boolean returnImmediately
) {}

