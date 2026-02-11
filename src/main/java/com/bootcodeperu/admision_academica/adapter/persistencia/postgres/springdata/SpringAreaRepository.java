package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcodeperu.admision_academica.domain.model.Area;
import org.springframework.data.jpa.repository.Query;

@JaversSpringDataAuditable
public interface SpringAreaRepository extends JpaRepository<Area, Long> {
    // Búsqueda global (ignora el estado activo)
    @Query("SELECT a FROM Area a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Area> findAllWithSearch(String search, Pageable pageable);

    // Búsqueda filtrada por activos
    @Query("SELECT a FROM Area a WHERE a.activo = true AND LOWER(a.nombre) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Area> findAllActiveWithSearch(String search, Pageable pageable);

    Optional<Area> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    boolean existsByDescripcion(String descripcion);

    boolean existsByNombreAndIdNot(String nombre, Long id);
}
