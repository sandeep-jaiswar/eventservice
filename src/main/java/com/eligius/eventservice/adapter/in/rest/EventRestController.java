package com.eligius.eventservice.adapter.in.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eligius.eventservice.application.service.EventService;
import com.eligius.eventservice.domain.model.Event;

@RestController
@RequestMapping("/event")
public class EventRestController {
    @Autowired
    private EventService eventService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody @Validated Event event) {
        try {
            eventService.publishEvent(event.getEventType(), event.getEventKey(), event.getEventValue());
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Event published successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to publish event: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
