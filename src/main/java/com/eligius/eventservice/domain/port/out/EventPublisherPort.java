package com.eligius.eventservice.domain.port.out;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.support.SendResult;

import com.eligius.eventservice.domain.model.Event;

public interface EventPublisherPort {
    CompletableFuture<SendResult<String, String>> publish(Event event);
}
