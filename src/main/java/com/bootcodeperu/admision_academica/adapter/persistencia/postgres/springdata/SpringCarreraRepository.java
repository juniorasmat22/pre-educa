package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import com.bootcodeperu.admision_academica.domain.model.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringCarreraRepository extends JpaRepository<Carrera, Long> {
    List<Carrera> findByAreaId(Long areaId);
    boolean existsByNombre(String nombre);
}
