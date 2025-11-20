package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcodeperu.admision_academica.domain.model.Outbox;

public interface SpringOutboxRepository extends JpaRepository<Outbox, Long>{
	/**
     * Consulta para el Scheduler. Busca los primeros 50 eventos
     * en estado PENDIENTE, ordenados por fecha de creación ascendente.
     */
	List<Outbox> findTop50ByEstadoOrderByFechaCreacionAsc(String estado);
}
