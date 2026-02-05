package com.bootcodeperu.admision_academica.domain.repository;

import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.Curso;

public interface CursoRepository {
	Curso save(Curso curso);
    Optional<Curso> findById(Long id);
    List<Curso> findAll();
    Optional<Curso> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    boolean existsByDescripcion(String descripcion);
    boolean existsById(Long id);
}
