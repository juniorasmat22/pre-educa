package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.mapper.AreaMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.page.PageResponse;
import com.bootcodeperu.admision_academica.application.service.AreaService;
import com.bootcodeperu.admision_academica.domain.exception.DuplicateResourceException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.Area;
import com.bootcodeperu.admision_academica.domain.model.Universidad;
import com.bootcodeperu.admision_academica.domain.repository.AreaRepository;
import com.bootcodeperu.admision_academica.domain.repository.UniversidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AreaUseCase implements AreaService {
    private final AreaRepository areaRepository;
    private final UniversidadRepository universidadRepository;
    private final AreaMapper areaMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AreaResponse> getAreasPaged(String search, boolean includeInactive, Pageable pageable) {
        Page<Area> areas = includeInactive
                ? areaRepository.findAll(search, pageable)
                : areaRepository.findAllActive(search, pageable);

        return PageResponse.from(areas, areaMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaResponse> getAllAreas() {
        return areaRepository.findAll().stream().map(areaMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AreaResponse findAreaById(Long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada con ID: " + id));

        return areaMapper.toResponse(area);
    }

    @Override
    @Transactional
    public AreaResponse createArea(AreaRequest request) {
        if (areaRepository.existsByNombre(request.nombre())) {
            throw new DuplicateResourceException("El nombre del área ya existe");
        }
        Universidad universidad = universidadRepository.findById(request.idUniversidad())
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada con ID: " + request.idUniversidad()));
        Area area = areaMapper.toEntity(request);
        area.setUniversidad(universidad);
        return areaMapper.toResponse(areaRepository.save(area));
    }

    @Override
    @Transactional
    public AreaResponse updateArea(Long id, AreaRequest request) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada con ID: " + id));
        if (areaRepository.existsByNombreAndIdNot(request.nombre(), id)) {
            throw new DuplicateResourceException("El nombre del área ya existe");
        }
        Universidad universidad = universidadRepository.findById(request.idUniversidad())
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada con ID: " + request.idUniversidad()));

        area.setNombre(request.nombre());
        area.setDescripcion(request.descripcion());
        area.setPuntajeCorrecta(request.puntajeCorrecta());
        area.setPuntajeIncorrecta(request.puntajeIncorrecta());
        area.setPuntajeBlanco(request.puntajeBlanco());
        area.setDuracionMinutos(request.duracionMinutos());
        area.setUniversidad(universidad);
        return areaMapper.toResponse(areaRepository.save(area));
    }

    @Override
    @Transactional
    public void deleteArea(Long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada"));

        // Borrado Lógico: Javers registrará quién hizo este cambio de estado
        area.setActivo(false);
        areaRepository.save(area);
    }
}
