package com.bootcodeperu.admision_academica.domain.repository;

import com.bootcodeperu.admision_academica.domain.model.Carrera;

import java.util.List;
import java.util.Optional;

public interface CarreraRepository {
    Carrera save(Carrera carrera);
    Optional<Carrera> findById(Long id);
    List<Carrera> findAll();
    List<Carrera> findByAreaId(Long areaId);
    boolean existsByNombre(String nombre);
}
