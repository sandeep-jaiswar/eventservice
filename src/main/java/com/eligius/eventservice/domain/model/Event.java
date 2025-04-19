package com.eligius.eventservice.domain.model;

import lombok.Builder;

@Builder
public record Event(
    String type,
    String key,
    String payload
) {}
