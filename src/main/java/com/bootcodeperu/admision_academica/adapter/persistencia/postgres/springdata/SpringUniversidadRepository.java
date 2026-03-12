package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import com.bootcodeperu.admision_academica.domain.model.Universidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpringUniversidadRepository extends JpaRepository<Universidad, Long> {
    boolean existsByNombreIgnoreCase(String nombre);

    @Query("SELECT DISTINCT u FROM Universidad u LEFT JOIN FETCH u.areas a LEFT JOIN FETCH a.carreras WHERE u.activo = true")
    List<Universidad> findAllEstructuraActiva();
}
