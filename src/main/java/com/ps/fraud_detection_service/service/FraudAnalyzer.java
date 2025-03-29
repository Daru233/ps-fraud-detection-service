package com.ps.fraud_detection_service.service;

import com.ps.fraud_detection_service.dto.TransactionCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.List;
import java.util.concurrent.Executor;

@Service
public class FraudAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(FraudAnalyzer.class);

    private final BlockingQueue<TransactionCreatedEvent> fraudProcessingQueue;
    private final Executor fraudProcessorExecutor;

    public FraudAnalyzer(@Qualifier("fraudProcessingQueue") BlockingQueue<TransactionCreatedEvent> fraudProcessingQueue,
                         @Qualifier("fraudProcessorExecutor") Executor fraudProcessorExecutor) {
        this.fraudProcessingQueue = fraudProcessingQueue;
        this.fraudProcessorExecutor = fraudProcessorExecutor;
    }

    @Scheduled(fixedDelay = 2000)
    public void pollAndDispatch() {
        List<TransactionCreatedEvent> batch = new ArrayList<>();
        fraudProcessingQueue.drainTo(batch, 4);
        if (batch.isEmpty()) return;

        for (TransactionCreatedEvent event : batch) {
            fraudProcessorExecutor.execute(() -> processEvent(event));
        }
    }

    private void processEvent(TransactionCreatedEvent event) {
        try {
            log.info("[Thread: {}] Analyzing transaction {}", Thread.currentThread().getName(), event.transactionId());

            // Simulated fraud detection logic
            Thread.sleep(1000); // simulate processing delay


        } catch (Exception e) {
            log.error("Error analyzing transaction {}", event.transactionId(), e);
        }
    }


}
