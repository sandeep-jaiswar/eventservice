package com.eligius.eventservice.adapter.out.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.eligius.eventservice.application.service.InMemorySubscriptionManager;
import com.eligius.eventservice.domain.model.Event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventListener {

    private final InMemorySubscriptionManager subscriptionManager;

    @KafkaListener(topics = "#{'${spring.kafka.event.topics}'.split(',')}", groupId = "event-stream-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            Event event = Event.builder()
                    .type(record.topic())
                    .key(record.key())
                    .payload(record.value())
                    .build();

            log.info("Received from Kafka: {}", event);
            subscriptionManager.publishToSubscribers(event);
        } catch (Exception e) {
            log.error("Error processing Kafka record: {}", record, e);
        }
    }
}
