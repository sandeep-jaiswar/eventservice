package com.eligius.eventservice;

import com.eligius.eventservice.adapter.in.grpc.GrpcEventController;
import com.eligius.eventservice.application.service.EventService;
import com.eligius.eventservice.grpc.EventRequest;
import com.eligius.eventservice.grpc.EventResponse;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GrpcEventControllerTest {

    private EventService eventService;
    private GrpcEventController controller;

    @BeforeEach
    void setUp() {
        eventService = mock(EventService.class);
        controller = new GrpcEventController(eventService);
    }

    @Test
    void sendEvent_successfullyPublishesEvent() {
        EventRequest request = EventRequest.newBuilder()
                .setType("anonymous.user.created")
                .setKey("test-key")
                .setPayload("{\"userId\":123}")
                .build();

        StreamObserver<EventResponse> responseObserver = mock(StreamObserver.class);

        controller.sendEvent(request, responseObserver);

        verify(eventService).publishEvent("anonymous.user.created", "test-key", "{\"userId\":123}");
        ArgumentCaptor<EventResponse> captor = ArgumentCaptor.forClass(EventResponse.class);
        verify(responseObserver).onNext(captor.capture());
        assertEquals("PUBLISHED", captor.getValue().getStatus());
        verify(responseObserver).onCompleted();
    }

    @Test
    void sendEvent_handlesExceptionGracefully() {
        EventRequest request = EventRequest.newBuilder()
                .setType("anonymous.user.created")
                .setKey("fail-key")
                .setPayload("{\"userId\":456}")
                .build();

        doThrow(new RuntimeException("Kafka down")).when(eventService)
                .publishEvent("anonymous.user.created", "fail-key", "{\"userId\":456}");

        StreamObserver<EventResponse> responseObserver = mock(StreamObserver.class);

        controller.sendEvent(request, responseObserver);

        ArgumentCaptor<Throwable> errorCaptor = ArgumentCaptor.forClass(Throwable.class);
        verify(responseObserver).onError(errorCaptor.capture());

        Throwable thrown = errorCaptor.getValue();
        assertInstanceOf(StatusRuntimeException.class, thrown);
        assertTrue(thrown.getMessage().contains("Failed to publish event to Kafka"));
    }
}
