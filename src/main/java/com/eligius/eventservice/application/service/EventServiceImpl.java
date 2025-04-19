package com.eligius.eventservice.application.service;

import org.springframework.stereotype.Service;

import com.eligius.eventservice.domain.model.Event;
import com.eligius.eventservice.domain.port.in.EventCommandPort;
import com.eligius.eventservice.domain.port.out.EventPublisherPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventCommandPort {
    private final EventPublisherPort publisher;

    @Override
    public void sendEvent(Event event) {
        if (event == null) {
            log.error("Attempted to send null event");
            throw new IllegalArgumentException("Event cannot be null");
        }
        if (event.type() == null || event.type().isEmpty()) {
            log.error("Attempted to send event with null or empty type");
            throw new IllegalArgumentException("Event type cannot be null or empty");
        }
        log.debug("Sending event: type={}, key={}", event.type(), event.key());
        publisher.publish(event);
        log.debug("Event sent successfully: type={}, key={}", event.type(), event.key());
    }
}
