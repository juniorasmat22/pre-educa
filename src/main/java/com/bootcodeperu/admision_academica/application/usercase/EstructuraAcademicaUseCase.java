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
    private final AreaRepository areaRepository;
    private final CursoRepository cursoRepository;
    private final CursoAreaRepository cursoAreaRepository;
    private final TemaRepository temaRepository;
    private final AreaMapper areaMapper;
    private final CursoMapper cursoMapper;
    private final CursoAreaMapper cursoAreaMapper;
    private final TemaMapper temaMapper;
    @Override
    public List<AreaResponse> getAllAreas() {
        return areaRepository.findAll().stream()
                .map(areaMapper::toResponse)
                .collect(Collectors.toList());
    }
    @Override
    public AreaResponse findAreaById(Long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area", "id", id));
        return areaMapper.toResponse(area);
    }

    @Override
    public List<CursoResponse> getAllCursos() {
        // Asumiendo que CursoRepository tiene un findAll()
        return cursoRepository.findAll().stream().map(cursoMapper::toResponse).collect(Collectors.toList());
    }
    
    @Override
    public List<CursoAreaResponse> getDistribucionByArea(Long areaId) {
        return cursoAreaRepository.findAllByAreaId(areaId).stream()
                .map(cursoAreaMapper::toResponse)
                .collect(Collectors.toList());
    }
    @Override
    public AreaResponse saveArea(AreaRequest request) {
        //Validaciones de negocio (dominio)
        if (areaRepository.existsByNombre(request.nombre())) {
            throw new DuplicateResourceException(
                    "Ya existe un área con el nombre: " + request.nombre()
            );
        }
        if (areaRepository.existsByDescripcion(request.descripcion())) {
            throw new DuplicateResourceException(
                    "Ya existe un área con la descripción: " + request.descripcion()
            );
        }
        // Mapeo DTO → Entidad
        Area area = areaMapper.toEntity(request);
        //Persistencia
        Area areaGuardada = areaRepository.save(area);
        //Respuesta
        return areaMapper.toResponse(areaGuardada);
    }
    @Override
    public CursoResponse saveCurso(CursoRequest cursoRequest) {
        //Validaciones de negocio (dominio)
        if (cursoRepository.existsByNombre(cursoRequest.nombre())) {
            throw new DuplicateResourceException(
                    "Ya existe un área con el nombre: " + cursoRequest.nombre()
            );
        }
        if (cursoRepository.existsByDescripcion(cursoRequest.descripcion())) {
            throw new DuplicateResourceException(
                    "Ya existe un área con la descripción: " + cursoRequest.descripcion()
            );
        }
        try {
            Curso curso=cursoMapper.toEntity(cursoRequest);
            //Guardar la entidad en DB
            Curso cursoGuardado = cursoRepository.save(curso);
            //Verificar que se guardó correctamente
            if (cursoGuardado.getId() == null) {
                throw new RuntimeException("No se pudo crear el curso. Intenta nuevamente.");
            }
            //Convertir a DTO y devolver
            return cursoMapper.toResponse(cursoGuardado);
        } catch (Exception e) {
            //Manejo global de errores: registrar y lanzar excepción controlada
            throw new RuntimeException("Error al crear el curso: " + e.getMessage(), e);
        }
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
    @Override
    public TemaResponse saveTema(Tema tema) {
        //Validación básica
        if (tema == null) {
            throw new IllegalArgumentException("El objeto Tema no puede ser null");
        }
        //Validación de campos obligatorios
        if (tema.getNombreTema() == null || tema.getNombreTema().isBlank()) {
            throw new IllegalArgumentException("El nombre del tema es obligatorio");
        }
        if (tema.getCurso() == null || tema.getCurso().getId() == null) {
            throw new IllegalArgumentException("El curso asociado es obligatorio");
        }
        //Evitar duplicados por nombre en el mismo curso
//        if (temaRepository.existsByNombreAndCursoId(tema.getNombre(), tema.getCurso().getId())) {
//            throw new IllegalArgumentException("Ya existe un tema con ese nombre en este curso");
//        }
        try {
            //Guardar en DB
            Tema guardado = temaRepository.save(tema);
            //Verificación
            if (guardado.getId() == null) {
                throw new RuntimeException("No se pudo crear el tema");
            }
            //Mapear a DTO y devolver
            return temaMapper.toResponse(guardado);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el tema: " + e.getMessage(), e);
        }
    }

}
