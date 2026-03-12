package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.mapper.UniversidadMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.universidad.UniversidadRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.universidad.UniversidadResponse;
import com.bootcodeperu.admision_academica.application.service.UniversidadService;
import com.bootcodeperu.admision_academica.domain.exception.DuplicateResourceException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.Universidad;
import com.bootcodeperu.admision_academica.domain.repository.UniversidadRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UniversidadUseCase implements UniversidadService {
    private final UniversidadRepository universidadRepository;
    private final UniversidadMapper universidadMapper;

    @Override
    @Transactional
    public UniversidadResponse crear(UniversidadRequest request) {
        if (universidadRepository.existsByNombre(request.nombre())) {
            throw new DuplicateResourceException("Ya existe una universidad con el nombre: " + request.nombre());
        }

        Universidad universidad = universidadMapper.toEntity(request);
        universidad = universidadRepository.save(universidad);

        return universidadMapper.toResponse(universidad);
    }

    @Override
    @Transactional
    public UniversidadResponse actualizar(Long id, UniversidadRequest request) {
        Universidad universidad = universidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada con ID: " + id));

        // Validar si están intentando cambiar el nombre a uno que ya existe
        if (!universidad.getNombre().equalsIgnoreCase(request.nombre()) &&
                universidadRepository.existsByNombre(request.nombre())) {
            throw new DuplicateResourceException("Ya existe otra universidad con el nombre: " + request.nombre());
        }

        universidadMapper.updateEntity(request, universidad);
        universidad = universidadRepository.save(universidad);

        return universidadMapper.toResponse(universidad);
    }

    @Override
    @Transactional(readOnly = true)
    public UniversidadResponse obtenerPorId(Long id) {
        return universidadRepository.findById(id)
                .map(universidadMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UniversidadResponse> listarTodas() {
        return universidadRepository.findAll().stream()
                .map(universidadMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!universidadRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Universidad no encontrada con ID: " + id);
        }
        universidadRepository.deleteById(id);
    }
}
