package com.bootcodeperu.admision_academica.domain.repository;

import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.CursoArea;
import com.bootcodeperu.admision_academica.domain.model.CursoAreaId;

public interface CursoAreaRepository {
	CursoArea save(CursoArea cursoArea);
    Optional<CursoArea> findById(CursoAreaId id);
    
    // Método de negocio clave: Obtener la distribución de preguntas para un área específica.
    List<CursoArea> findAllByAreaId(Long areaId);
    boolean existsByCursoIdAndAreaId(Long cursoId, Long areaId);

    void deleteById(CursoAreaId id);
}
