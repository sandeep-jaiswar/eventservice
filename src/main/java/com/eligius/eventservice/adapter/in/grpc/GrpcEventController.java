package com.eligius.eventservice.adapter.in.grpc;

import com.eligius.eventservice.application.service.EventService;
import com.eligius.eventservice.grpc.EventRequest;
import com.eligius.eventservice.grpc.EventResponse;
import com.eligius.eventservice.grpc.EventServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class GrpcEventController extends EventServiceGrpc.EventServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(GrpcEventController.class);
    private final EventService eventService;

    public GrpcEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void sendEvent(EventRequest request, StreamObserver<EventResponse> responseObserver) {
        try {
    public void sendEvent(EventRequest request, StreamObserver<EventResponse> responseObserver) {
        try {
            // Validate request parameters
            if (request.getType() == null || request.getType().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Event type cannot be empty")
                        .asRuntimeException());
                return;
            }
            if (request.getKey() == null || request.getKey().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Event key cannot be empty")
                        .asRuntimeException());
                return;
            }
            if (request.getPayload() == null || request.getPayload().isEmpty()) {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Event payload cannot be empty")
                        .asRuntimeException());
                return;
            }

            eventService.publishEvent(request.getType(), request.getKey(), request.getPayload());

            EventResponse response = EventResponse.newBuilder()
                    .setStatus("PUBLISHED")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("Failed to publish event: type={}, key={}, payload={}",
                    request.getType(), request.getKey(), request.getPayload(), e);

            StatusRuntimeException statusException = Status.INTERNAL
                    .withDescription("Failed to publish event to Kafka")
                    .withCause(e)
                    .asRuntimeException();

            responseObserver.onError(statusException);
        }
    }
}
