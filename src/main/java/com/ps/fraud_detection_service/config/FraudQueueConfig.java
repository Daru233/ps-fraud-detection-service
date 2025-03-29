package com.ps.fraud_detection_service.config;

import com.ps.fraud_detection_service.dto.TransactionCreatedEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class FraudQueueConfig {

    @Bean(name = "fraudProcessingQueue")
    public BlockingQueue<TransactionCreatedEvent> fraudProcessingQueue() {
        return new LinkedBlockingQueue<>(); // thread safe, in memory queue
    }

    @Bean(name = "fraudProcessorExecutor")
    public Executor fraudProcessorExecutor() {
        return Executors.newFixedThreadPool(4);
    }

}

