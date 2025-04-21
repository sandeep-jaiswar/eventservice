package com.eligius.eventservice.domain.port.in;

import com.eligius.eventservice.domain.model.Event;

public interface EventCommandPort {
    void sendEvent(Event event);
}
