package com.bootcodeperu.admision_academica.application.usercase;

import java.util.List;
import java.util.stream.Collectors;

import com.bootcodeperu.admision_academica.adapter.mapper.AreaMapper;
import com.bootcodeperu.admision_academica.adapter.mapper.CursoAreaMapper;
import com.bootcodeperu.admision_academica.adapter.mapper.CursoMapper;
import com.bootcodeperu.admision_academica.adapter.mapper.TemaMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso.CursoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.curso.CursoResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CursoAreaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.tema.TemaResponse;
import com.bootcodeperu.admision_academica.domain.exception.DuplicateResourceException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.*;
import org.springframework.stereotype.Service;

import com.bootcodeperu.admision_academica.application.service.EstructuraAcademicaService;
import com.bootcodeperu.admision_academica.domain.repository.AreaRepository;
import com.bootcodeperu.admision_academica.domain.repository.CursoAreaRepository;
import com.bootcodeperu.admision_academica.domain.repository.CursoRepository;
import com.bootcodeperu.admision_academica.domain.repository.TemaRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class EstructuraAcademicaUseCase implements EstructuraAcademicaService{
	// Inyección de todos los repositorios de estructura
    private final CursoAreaRepository cursoAreaRepository;
    private final CursoAreaMapper cursoAreaMapper;

    @Override
    public List<CursoAreaResponse> getDistribucionByArea(Long areaId) {
        return cursoAreaRepository.findAllByAreaId(areaId).stream()
                .map(cursoAreaMapper::toResponse)
                .collect(Collectors.toList());
    }
    @Override
    public CursoAreaResponse saveCursoArea(CursoArea cursoArea) {
        //Validación básica
        if (cursoArea == null) {
            throw new IllegalArgumentException("El objeto CursoArea no puede ser null");
        }

        //Validación de relaciones obligatorias
        if (cursoArea.getCurso() == null || cursoArea.getCurso().getId() == null) {
            throw new IllegalArgumentException("El curso es obligatorio");
        }
        if (cursoArea.getArea() == null || cursoArea.getArea().getId() == null) {
            throw new IllegalArgumentException("El área es obligatoria");
        }

        //Validación de cantidad de preguntas
        if (cursoArea.getCantidadPreguntas() == null || cursoArea.getCantidadPreguntas() < 0) {
            throw new IllegalArgumentException("La cantidad de preguntas debe ser 0 o mayor");
        }

        //Evitar duplicados: si ya existe un CursoArea con la misma llave compuesta
//        CursoAreaId id = new CursoAreaId(cursoArea.getCurso().getId(), cursoArea.getArea().getId());
//        if (cursoAreaRepository.existsById(id)) {
//            throw new IllegalArgumentException("Ya existe una distribución de curso para esta área");
//        }

        try {
            //Guardar la entidad
            CursoArea guardado = cursoAreaRepository.save(cursoArea);

            //Verificar que se guardó correctamente
            if (guardado.getId() == null) {
                throw new RuntimeException("No se pudo crear la distribución del curso");
            }

            //Convertir a DTO
            return cursoAreaMapper.toResponse(guardado);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear la distribución del curso: " + e.getMessage(), e);
        }
    }
}
