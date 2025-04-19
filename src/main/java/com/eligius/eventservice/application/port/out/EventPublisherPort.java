package com.eligius.eventservice.application.port.out;

import com.eligius.eventservice.domain.model.Event;

public interface EventPublisherPort {
    void publish(Event event);
}
