package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcodeperu.admision_academica.domain.model.Curso;

public interface SpringCursoRepository extends JpaRepository<Curso, Long> {
    Optional<Curso> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    boolean existsByDescripcion(String descripcion);
}