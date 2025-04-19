package com.eligius.eventservice.domain.port.out;

import com.eligius.eventservice.domain.model.Event;

public interface EventPublisherPort {
    void publish(Event event);
}
