package com.eligius.eventservice.domain.port.in;

import com.eligius.eventservice.domain.model.Event;

import io.grpc.stub.StreamObserver;

public interface EventSubscriberPort {
    /**
     * Subscribes to events on a specific topic.
     *
     * @param topic the topic to subscribe to
     * @param eventConsumer the consumer to handle the events
     * @return a subscription ID that can be used to unsubscribe
     */
    String subscribe(String topic, StreamObserver<Event> observer);
    
    /**
     * Unsubscribes from a topic.
     *
     * @param subscriptionId the subscription ID returned from subscribe
     * @return true if unsubscribed successfully, false if the subscription ID was not found
     */
    boolean unsubscribe(String subscriptionId);
}
