package com.bootcodeperu.admision_academica.application.service.outbox;

import com.bootcodeperu.admision_academica.domain.model.Outbox;
import com.bootcodeperu.admision_academica.domain.model.enums.OutboxEventType;

public interface OutboxHandler {
    boolean supports(OutboxEventType type);

    void handle(Outbox event) throws Exception;
}