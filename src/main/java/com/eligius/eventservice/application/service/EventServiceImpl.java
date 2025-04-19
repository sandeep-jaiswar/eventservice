package com.eligius.eventservice.application.service;

import org.springframework.stereotype.Service;

import com.eligius.eventservice.domain.model.Event;
import com.eligius.eventservice.domain.port.in.EventCommandPort;
import com.eligius.eventservice.domain.port.out.EventPublisherPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventCommandPort {
    private final EventPublisherPort publisher;

    @Override
    public void sendEvent(Event event) {
        publisher.publish(event);
    }
}
