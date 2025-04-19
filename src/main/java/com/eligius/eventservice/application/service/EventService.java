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

    public void publishEvent(String eventType, String eventKey, String eventValue) {
        // Validate input parameters
        if (eventType == null || eventType.isEmpty()) {
            throw new IllegalArgumentException("Event type cannot be null or empty");
        }
        if (eventKey == null || eventKey.isEmpty()) {
            throw new IllegalArgumentException("Event key cannot be null or empty");
        }
        if (eventValue == null) {
            throw new IllegalArgumentException("Event value cannot be null");
        }

        Event event = new Event(eventType, eventKey, eventValue);
        try {
            publisher.publish(event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish event: " + e.getMessage(), e);
        }
    }
}
