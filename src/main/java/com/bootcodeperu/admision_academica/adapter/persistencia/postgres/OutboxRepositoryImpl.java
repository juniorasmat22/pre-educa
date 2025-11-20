package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringOutboxRepository;
import com.bootcodeperu.admision_academica.domain.model.Outbox;
import com.bootcodeperu.admision_academica.domain.repository.OutboxRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutboxRepositoryImpl implements OutboxRepository {

    private final SpringOutboxRepository springOutboxRepository;

    @Override
    public Outbox save(Outbox outbox) {
        return springOutboxRepository.save(outbox);
    }

    @Override
    public List<Outbox> findTopPendingEvents() {
        // Llama al método nombrado en Spring Data JPA para obtener los eventos pendientes
        // El estado 'PENDIENTE' es una constante definida implícitamente en el uso.
        return springOutboxRepository.findTop50ByEstadoOrderByFechaCreacionAsc("PENDIENTE");
    }
}