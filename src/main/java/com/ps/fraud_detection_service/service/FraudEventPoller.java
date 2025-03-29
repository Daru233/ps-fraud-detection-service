package com.ps.fraud_detection_service.service;

import com.google.api.client.util.Value;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.AcknowledgeablePubsubMessage;
import com.google.gson.Gson;
import com.ps.fraud_detection_service.config.PropertyLoggerConfig;
import com.ps.fraud_detection_service.dto.TransactionCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Component
public class FraudEventPoller {

    private static final Logger log = LoggerFactory.getLogger(PropertyLoggerConfig.class);

    private final PubSubTemplate pubSubTemplate;
    private final Gson gson = new Gson();
    private final BlockingQueue<TransactionCreatedEvent> fraudProcessingQueue;

    @Value("${fraud.subscription}")
    private String subscription;

    @Value("${fraud.max-messages}")
    private int maxMessages;

    public FraudEventPoller(PubSubTemplate pubSubTemplate,
                            @Qualifier("fraudProcessingQueue") BlockingQueue<TransactionCreatedEvent> fraudProcessingQueue) {
        this.pubSubTemplate = pubSubTemplate;
        this.fraudProcessingQueue = fraudProcessingQueue;
    }

    @Scheduled(cron = "${fraud.cron}", zone = "${fraud.zone}")
    public void pollTransactionEvents() {
        List<AcknowledgeablePubsubMessage> messages = pubSubTemplate.pull(subscription, maxMessages, true);

        if (messages.isEmpty()) return;

        for (AcknowledgeablePubsubMessage msg : messages) {
            try {
                String payload = msg.getPubsubMessage().getData().toStringUtf8();
                TransactionCreatedEvent event = gson.fromJson(payload, TransactionCreatedEvent.class);
                log.debug("Adding transaction {} to queue", event.transactionId());
                fraudProcessingQueue.add(event);
                msg.ack();
            } catch (Exception e) {
                log.error("Error processing transaction {}", e.getMessage());
                msg.nack();
            }
        }
    }
}

