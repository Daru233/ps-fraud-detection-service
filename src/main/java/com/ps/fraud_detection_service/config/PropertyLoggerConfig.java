package com.ps.fraud_detection_service.config;

import com.ps.fraud_detection_service.dto.FraudProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyLoggerConfig {

    private static final Logger log = LoggerFactory.getLogger(PropertyLoggerConfig.class);

    @Bean
    public ApplicationRunner logFraudProperties(FraudProperties fraud) {
        return args -> {
            log.info("✅ Loaded FraudProperties:");
            log.info("  ↳ cron              : {}", fraud.cron());
            log.info("  ↳ zone              : {}", fraud.zone());
            log.info("  ↳ subscription      : {}", fraud.subscription());
            log.info("  ↳ maxMessages       : {}", fraud.maxMessages());
            log.info("  ↳ returnImmediately : {}", fraud.returnImmediately());
        };
    }
}

