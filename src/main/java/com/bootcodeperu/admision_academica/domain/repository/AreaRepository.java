package com.bootcodeperu.admision_academica.domain.repository;


import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AreaRepository {
    Area save(Area area);

    Optional<Area> findById(Long id);

    List<Area> findAll();

    // Paginación y búsqueda global (Activos e Inactivos)
    Page<Area> findAll(String search, Pageable pageable);

    // Paginación y búsqueda solo activos
    Page<Area> findAllActive(String search, Pageable pageable);

    // Método de negocio clave: Encontrar un área por su nombre (ej: "A. Ciencias...")
    Optional<Area> findByName(String name);

    boolean existsByNombre(String nombre);

    boolean existsByDescripcion(String descripcion);

    boolean existsByNombreAndIdNot(String nombre, Long id);

}
