package com.eligius.eventservice.adapter.in.rest;

import org.springframework.beans.factory.annotation.Autowired;
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
    public String sendMessage(@RequestBody Event event) {
        eventService.publishEvent(event.getEventType(), event.getEventKey(), event.getEventValue());
        return "Message sent: " + event.getEventValue();
    }
}
