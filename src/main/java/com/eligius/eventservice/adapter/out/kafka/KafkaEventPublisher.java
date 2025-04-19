package com.eligius.eventservice.adapter.out.kafka;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.eligius.eventservice.domain.model.Event;
import com.eligius.eventservice.domain.port.out.EventPublisherPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisherPort {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public CompletableFuture<SendResult<String, String>> publish(Event event) {
        log.info("Publishing event to Kafka: {}", event);
        return kafkaTemplate.send(event.type(), event.key(), event.payload())
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event to Kafka: {}", event, ex);
                    } else {
                        log.debug("Successfully published event to Kafka: {} at offset {}",
                                event, result.getRecordMetadata().offset());
                    }
                });
    }
}
