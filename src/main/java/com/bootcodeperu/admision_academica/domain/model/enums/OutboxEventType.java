package com.bootcodeperu.admision_academica.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OutboxEventType {
    QUESTION_CREATED("PREGUNTA_CREADA_EVENT"),
    FLASHCARD_CREATED("FLASHCARD_CREADA_EVENT"),
    THEORY_CREATED("TEORIA_CREADA_EVENT");

    private final String value;

    public static OutboxEventType fromValue(String value) {
        for (OutboxEventType type : OutboxEventType.values()) {
            if (type.value.equalsIgnoreCase(value)) return type;
        }
        throw new IllegalArgumentException("Unknown event type: " + value);
    }
}