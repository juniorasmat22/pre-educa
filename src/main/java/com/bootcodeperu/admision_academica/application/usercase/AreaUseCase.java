package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.mapper.AreaMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaResponse;
import com.bootcodeperu.admision_academica.application.service.AreaService;
import com.bootcodeperu.admision_academica.domain.exception.DuplicateResourceException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.Area;
import com.bootcodeperu.admision_academica.domain.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AreaUseCase implements AreaService {
    private final AreaRepository areaRepository;
    private final AreaMapper areaMapper;

    @Override
    public List<AreaResponse> getAllAreas() {
        return areaRepository.findAll().stream().map(areaMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public AreaResponse findAreaById(Long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada con ID: " + id));

        return areaMapper.toResponse(area);
    }

    @Override
    public AreaResponse createArea(AreaRequest request) {
        if (areaRepository.existsByNombre(request.nombre())) {
            throw new DuplicateResourceException("El nombre del área ya existe");
        }
        Area area = areaMapper.toEntity(request);
        return areaMapper.toResponse(areaRepository.save(area));
    }

    @Override
    public AreaResponse updateArea(Long id, AreaRequest request) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada con ID: " + id));
        if (areaRepository.existsByNombreAndIdNot(request.nombre(), id)) {
            throw new DuplicateResourceException("El nombre del área ya existe");
        }
        area.setNombre(request.nombre());
        area.setDescripcion(request.descripcion());
        area.setPuntajeCorrecta(request.puntajeCorrecta());
        area.setPuntajeIncorrecta(request.puntajeIncorrecta());
        area.setPuntajeBlanco(request.puntajeBlanco());
        return areaMapper.toResponse(areaRepository.save(area));
    }
}
