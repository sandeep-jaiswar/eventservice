package com.eligius.eventservice.application.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import com.eligius.eventservice.domain.model.Event;
import com.eligius.eventservice.domain.port.in.EventSubscriberPort;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InMemorySubscriptionManager implements EventSubscriberPort {

    private final Map<String, List<StreamObserver<Event>>> topicSubscribers = new ConcurrentHashMap<>();
    private final Map<String, StreamObserver<Event>> subscriptionRegistry = new ConcurrentHashMap<>();

    @Override
    public String subscribe(String topic, StreamObserver<Event> observer) {
        topicSubscribers
                .computeIfAbsent(topic, t -> new CopyOnWriteArrayList<>())
                .add(observer);

        String subscriptionId = UUID.randomUUID().toString();
        subscriptionRegistry.put(subscriptionId, observer);
        return subscriptionId;
    }

    @Override
    public boolean unsubscribe(String subscriptionId) {
        StreamObserver<Event> observer = subscriptionRegistry.remove(subscriptionId);
        if (observer == null) {
            return false;
        }

        // Remove the observer from all topics (in case it's subscribed to multiple)
        for (List<StreamObserver<Event>> observers : topicSubscribers.values()) {
            observers.remove(observer);
        }

        return true;
    }

    public void publishToSubscribers(Event event) {
        List<StreamObserver<Event>> observers = topicSubscribers.getOrDefault(event.type(), List.of());
        for (var observer : observers) {
            try {
                observer.onNext(event);
            } catch (Exception e) {
                log.error("Failed to notify observer for event type {}: {}", event.type(), e.getMessage(), e);
            }
        }
    }
}
