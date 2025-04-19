package com.eligius.eventservice.adapter.out.kafka;

import org.springframework.kafka.core.KafkaTemplate;
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
    public void publish(Event event) {
        log.info("Publishing event to Kafka: {}", event);
        kafkaTemplate.send(event.type(), event.key(), event.payload());
    }
}
