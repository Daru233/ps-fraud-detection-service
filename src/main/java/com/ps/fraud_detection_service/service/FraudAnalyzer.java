package com.ps.fraud_detection_service.service;

import com.ps.fraud_detection_service.dto.TransactionCreatedEvent;
import com.ps.fraud_detection_service.dto.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.Random;

@Service
public class FraudAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(FraudAnalyzer.class);

    private static final Random RANDOM = new Random();
    private static final int FRAUD_DETECTION_ODDS = 6;
    private final BlockingQueue<TransactionCreatedEvent> fraudProcessingQueue;
    private final Executor fraudProcessorExecutor;
    private final TransactionClient transactionClient;

    public FraudAnalyzer(@Qualifier("fraudProcessingQueue") BlockingQueue<TransactionCreatedEvent> fraudProcessingQueue,
                         @Qualifier("fraudProcessorExecutor") Executor fraudProcessorExecutor,
                         TransactionClient transactionClient) {
        this.fraudProcessingQueue = fraudProcessingQueue;
        this.fraudProcessorExecutor = fraudProcessorExecutor;
        this.transactionClient = transactionClient;
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
            Thread.sleep(1000); // simulate processing delay
            transactionClient.updateTransactionStatus(event.transactionId(), fraudDetectionLogic());
        } catch (Exception e) {
            log.error("Error analyzing transaction {}", event.transactionId(), e);
        }
    }

    private static TransactionStatus fraudDetectionLogic() {
        return RANDOM.nextInt(FRAUD_DETECTION_ODDS) == 0
                ? TransactionStatus.DECLINED
                : TransactionStatus.APPROVED;
    }

}
