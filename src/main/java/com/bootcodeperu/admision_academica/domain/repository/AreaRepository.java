package com.bootcodeperu.admision_academica.domain.repository;

import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.Area;

public interface AreaRepository {
	Area save(Area area);
    Optional<Area> findById(Long id);
    List<Area> findAll();
    // Método de negocio clave: Encontrar un área por su nombre (ej: "A. Ciencias...")
    Optional<Area> findByName(String name);
    boolean existsByNombre(String nombre);
    boolean existsByDescripcion(String descripcion);

}
