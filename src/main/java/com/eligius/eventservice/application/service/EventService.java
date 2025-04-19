package com.eligius.eventservice.application.service;

import org.springframework.stereotype.Service;

import com.eligius.eventservice.application.port.out.EventPublisherPort;
import com.eligius.eventservice.domain.model.Event;

@Service
public class EventService {
    private final EventPublisherPort publisher;

    public EventService(EventPublisherPort publisher) {
        this.publisher = publisher;
    }

package com.eligius.eventservice.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.eligius.eventservice.application.port.out.EventPublisherPort;
import com.eligius.eventservice.domain.model.Event;

@Service
public class EventService {
    private static final Logger log = LoggerFactory.getLogger(EventService.class);
    private final EventPublisherPort publisher;

    public EventService(EventPublisherPort publisher) {
        this.publisher = publisher;
    }

    public void publishEvent(String eventType, String eventKey, String eventValue) {
        log.debug("Attempting to publish event - type: {}, key: {}", eventType, eventKey);
        // Validate input parameters
        if (eventType == null || eventType.isEmpty()) {
            log.warn("Attempt to publish event with null or empty event type");
            throw new IllegalArgumentException("Event type cannot be null or empty");
        }
        if (eventKey == null || eventKey.isEmpty()) {
            log.warn("Attempt to publish event with null or empty event key");
            throw new IllegalArgumentException("Event key cannot be null or empty");
        }
        if (eventValue == null) {
            log.warn("Attempt to publish event with null event value");
            throw new IllegalArgumentException("Event value cannot be null");
        }

        Event event = new Event(eventType, eventKey, eventValue);
        log.info("Publishing event: {}", event);
        try {
            publisher.publish(event);
            log.debug("Event successfully passed to publisher");
        } catch (Exception e) {
            log.error("Failed to publish event: {}", event, e);
            throw new RuntimeException("Failed to publish event: " + e.getMessage(), e);
        }
    }
}
}
