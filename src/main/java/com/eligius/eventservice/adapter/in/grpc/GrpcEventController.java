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
