package com.eligius.eventservice.application.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import com.eligius.eventservice.domain.model.Event;
import com.eligius.eventservice.domain.port.in.EventSubscriberPort;

import io.grpc.stub.StreamObserver;

@Service
public class InMemorySubscriptionManager implements EventSubscriberPort {
    

        private final ConcurrentHashMap<String, List<StreamObserver<Event>>> topicSubscribers = new ConcurrentHashMap<>();

    @Override
    public void subscribe(String topic, StreamObserver<Event> observer) {
        topicSubscribers
            .computeIfAbsent(topic, t -> new CopyOnWriteArrayList<>())
            .add(observer);
    }

    public void publishToSubscribers(Event event) {
        List<StreamObserver<Event>> observers = topicSubscribers.getOrDefault(event.type(), List.of());
        for (var observer : observers) {
            observer.onNext(event);
        }
    }
}
