package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CreateCursoAreaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CursoAreaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CursoAreaSimpleResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.UpdateCursoAreaRequest;

import java.util.List;

public interface CursoAreaService {
    // Crear una distribución curso-area
    CursoAreaResponse create(CreateCursoAreaRequest request);

    // Obtener por ID compuesto
    CursoAreaSimpleResponse findById(Long cursoId, Long areaId);

    // Obtener todas las distribuciones de un área
    List<CursoAreaSimpleResponse> findByArea(Long areaId);

    // Actualizar cantidad de preguntas de un CursoArea
    CursoAreaResponse update(Long cursoId, Long areaId, UpdateCursoAreaRequest request);

    // Eliminar una distribución
    void delete(Long cursoId, Long areaId);
}
