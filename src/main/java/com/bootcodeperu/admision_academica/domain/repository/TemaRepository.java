package com.bootcodeperu.admision_academica.domain.repository;

import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.Tema;

public interface TemaRepository {
	Tema save(Tema tema);
    Optional<Tema> findById(Long id);
    List<Tema> findAllByCursoId(Long cursoId); // Obtener todos los temas de un curso específico
    List<Long> findIdsByCursoId(Long cursoId); // Obtener solo los IDs de los temas de un curso (útil para simulacros)
}
