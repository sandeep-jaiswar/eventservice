package com.eligius.eventservice.adapter.in.grpc;

import java.time.Instant;

import com.eligius.eventservice.domain.model.Event;
import com.eligius.eventservice.domain.port.in.EventCommandPort;
import com.eligius.eventservice.grpc.EventRequest;
import com.eligius.eventservice.grpc.EventResponse;
import com.eligius.eventservice.grpc.EventServiceGrpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class EventGrpcAdapter extends EventServiceGrpc.EventServiceImplBase {
        private final EventCommandPort commandPort;

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
}
