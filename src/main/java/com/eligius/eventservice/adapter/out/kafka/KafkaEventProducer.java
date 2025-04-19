package com.eligius.eventservice.adapter.out.kafka;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import com.eligius.eventservice.application.port.out.EventPublisherPort;
import com.eligius.eventservice.domain.model.Event;

@Component
public class KafkaEventProducer implements EventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(Event event) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(event.getEventType(),
                event.getEventKey(), event.getEventValue());

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish event to Kafka: {}", event, ex);
            } else {
                log.info("Published event to Kafka: {}", event);
            }
        });
    }
}
