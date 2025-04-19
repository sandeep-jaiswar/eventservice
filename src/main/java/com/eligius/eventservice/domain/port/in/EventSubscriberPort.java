package com.eligius.eventservice.domain.port.in;

import com.eligius.eventservice.domain.model.Event;

import io.grpc.stub.StreamObserver;

public interface EventSubscriberPort {
    void subscribe(String topic, StreamObserver<Event> observer);
}
