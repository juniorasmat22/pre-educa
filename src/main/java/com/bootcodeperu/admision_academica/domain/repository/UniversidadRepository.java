package com.bootcodeperu.admision_academica.domain.repository;

import com.bootcodeperu.admision_academica.domain.model.Universidad;

import java.util.List;
import java.util.Optional;

public interface UniversidadRepository {
    Universidad save(Universidad universidad);

    Optional<Universidad> findById(Long id);

    List<Universidad> findAll();

    void deleteById(Long id);

    boolean existsByNombre(String nombre);
    
    List<Universidad> obtenerEstructuraActiva();
}