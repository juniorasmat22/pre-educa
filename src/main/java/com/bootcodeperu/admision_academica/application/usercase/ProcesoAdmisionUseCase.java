package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.mapper.ProcesoAdmisionMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.procesoadmision.ProcesoAdmisionRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.procesoadmision.ProcesoAdmisionResponse;
import com.bootcodeperu.admision_academica.application.service.ProcesoAdmisionService;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.ProcesoAdmision;
import com.bootcodeperu.admision_academica.domain.model.Universidad;
import com.bootcodeperu.admision_academica.domain.repository.ProcesoAdmisionRepository;
import com.bootcodeperu.admision_academica.domain.repository.UniversidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcesoAdmisionUseCase implements ProcesoAdmisionService {

    private final ProcesoAdmisionRepository procesoRepository;
    private final UniversidadRepository universidadRepository;
    private final ProcesoAdmisionMapper mapper;

    @Override
    @Transactional
    public ProcesoAdmisionResponse crear(ProcesoAdmisionRequest request) {
        Universidad universidad = universidadRepository.findById(request.idUniversidad())
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada"));

        ProcesoAdmision proceso = mapper.toEntity(request);
        proceso.setUniversidad(universidad);
        return mapper.toResponse(procesoRepository.save(proceso));
    }

    @Override
    @Transactional
    public ProcesoAdmisionResponse actualizar(Long id, ProcesoAdmisionRequest request) {
        ProcesoAdmision proceso = procesoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proceso no encontrado"));

        Universidad universidad = universidadRepository.findById(request.idUniversidad())
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada"));

        mapper.updateEntity(request, proceso);
        proceso.setUniversidad(universidad);
        return mapper.toResponse(procesoRepository.save(proceso));
    }

    @Override
    public ProcesoAdmisionResponse obtenerPorId(Long id) {
        return procesoRepository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Proceso no encontrado"));
    }

    @Override
    public List<ProcesoAdmisionResponse> listarTodos() {
        return procesoRepository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        ProcesoAdmision proceso = procesoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proceso no encontrado"));
        proceso.setVigente(false); // Borrado lógico
        procesoRepository.save(proceso);
    }
}
