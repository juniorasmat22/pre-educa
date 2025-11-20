package com.bootcodeperu.admision_academica.domain.repository;

import java.util.List;

import com.bootcodeperu.admision_academica.domain.model.Outbox;

public interface OutboxRepository {

    Outbox save(Outbox outbox);
    
    /**
     * Obtiene una cantidad limitada de eventos pendientes
     * para ser procesados por el Scheduler.
     */
    List<Outbox> findTopPendingEvents(); 
    
    // El método saveAll es heredado implícitamente si se necesita para actualizar varios.
}