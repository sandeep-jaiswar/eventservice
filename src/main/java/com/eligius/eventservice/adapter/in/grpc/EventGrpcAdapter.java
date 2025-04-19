package com.eligius.eventservice.adapter.in.grpc;

import java.time.Instant;

import com.eligius.eventservice.domain.model.Event;
import com.eligius.eventservice.domain.port.in.EventCommandPort;
import com.eligius.eventservice.domain.port.in.EventSubscriberPort;
import com.eligius.eventservice.grpc.EventFilter;
import com.eligius.eventservice.grpc.EventRequest;
import com.eligius.eventservice.grpc.EventResponse;
import com.eligius.eventservice.grpc.EventServiceGrpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class EventGrpcAdapter extends EventServiceGrpc.EventServiceImplBase {
    private final EventCommandPort commandPort;
    private final EventSubscriberPort eventSubscriberPort;

    @Override
    public void sendEvent(EventRequest request, StreamObserver<EventResponse> responseObserver) {
        commandPort.sendEvent(Event.builder()
                .type(request.getType())
                .key(request.getKey())
                .payload(request.getPayload())
                .build());

        EventResponse response = EventResponse.newBuilder()
                .setStatus("OK")
                .setTopic(request.getType())
                .setReceivedPayload(request.getPayload())
                .setTimestamp(Instant.now().toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void streamEvents(EventFilter request, StreamObserver<EventResponse> responseObserver) {
        eventSubscriberPort.subscribe(request.getTopic(), new StreamObserver<>() {
            @Override
            public void onNext(Event event) {
                responseObserver.onNext(EventResponse.newBuilder()
                        .setStatus("NEW")
                        .setTopic(event.type())
                        .setReceivedPayload(event.payload())
                        .setTimestamp(String.valueOf(System.currentTimeMillis()))
                        .build());
            }

            @Override
            public void onError(Throwable t) {
                log.error("Error in streaming to frontend", t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        });
    }

}
