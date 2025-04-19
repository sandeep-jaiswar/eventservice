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
        Event event = new Event(eventType, eventKey, eventValue);
        publisher.publish(event);
    }
}
