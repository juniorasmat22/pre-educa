package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcodeperu.admision_academica.domain.model.Area;

public interface SpringAreaRepository extends JpaRepository<Area, Long> {
    Optional<Area> findByNombre(String nombre);
}
