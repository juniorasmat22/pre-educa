package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.mapper.CursoAreaMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CreateCursoAreaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CursoAreaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CursoAreaSimpleResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.UpdateCursoAreaRequest;
import com.bootcodeperu.admision_academica.application.service.CursoAreaService;
import com.bootcodeperu.admision_academica.domain.exception.DuplicateResourceException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.Area;
import com.bootcodeperu.admision_academica.domain.model.Curso;
import com.bootcodeperu.admision_academica.domain.model.CursoArea;
import com.bootcodeperu.admision_academica.domain.model.CursoAreaId;
import com.bootcodeperu.admision_academica.domain.repository.AreaRepository;
import com.bootcodeperu.admision_academica.domain.repository.CursoAreaRepository;
import com.bootcodeperu.admision_academica.domain.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CursoAreaUseCase implements CursoAreaService {
    private final CursoAreaRepository cursoAreaRepository;
    private final CursoRepository cursoRepository;
    private final AreaRepository areaRepository;
    private final CursoAreaMapper mapper;


    public CursoAreaResponse create(CreateCursoAreaRequest request) {

        if (cursoAreaRepository.existsByCursoIdAndAreaId(
                request.cursoId(), request.areaId())) {
            throw new DuplicateResourceException(
                    "Ya existe una distribución para este curso y área"
            );
        }

        Area area = areaRepository.findById(request.areaId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Área no encontrada"));

        Curso curso = cursoRepository.findById(request.cursoId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Curso no encontrado"));

        CursoArea cursoArea = new CursoArea(
                area,
                curso,
                request.cantidadPreguntas()
        );

        return mapper.toResponse(cursoAreaRepository.save(cursoArea));
    }

    @Transactional(readOnly = true)
    public CursoAreaSimpleResponse findById(Long cursoId, Long areaId) {
        CursoAreaId id = new CursoAreaId(areaId, cursoId);

        CursoArea entity = cursoAreaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Distribución no encontrada"));

        return mapper.toSimpleResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<CursoAreaSimpleResponse> findByArea(Long areaId) {
        return cursoAreaRepository.findAllByAreaId(areaId)
                .stream()
                .map(mapper::toSimpleResponse)
                .toList();
    }

    public CursoAreaResponse update(
            Long cursoId,
            Long areaId,
            UpdateCursoAreaRequest request) {

        CursoAreaId id = new CursoAreaId(areaId, cursoId);

        CursoArea entity = cursoAreaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Distribución no encontrada"));

        entity.actualizarCantidadPreguntas(request.cantidadPreguntas());

        return mapper.toResponse(cursoAreaRepository.save(entity));
    }

    public void delete(Long cursoId, Long areaId) {
        CursoAreaId id = new CursoAreaId(areaId, cursoId);

        CursoArea entity = cursoAreaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Distribución no encontrada"));

        cursoAreaRepository.deleteById(id);
    }
}
