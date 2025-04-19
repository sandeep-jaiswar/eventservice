package com.eligius.eventservice.adapter.out.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

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
    ListenableFuture<SendResult<String, String>> future =
        kafkaTemplate.send(event.getEventType(), event.getEventKey(), event.getEventValue());

    future.addCallback(
        result -> log.info("Published event to Kafka: {}", event),
        ex -> log.error("Failed to publish event to Kafka: {}", event, ex)
    );
}
}
