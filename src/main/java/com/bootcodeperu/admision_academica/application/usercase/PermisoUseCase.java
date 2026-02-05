package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.mapper.PermisoMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoResponse;
import com.bootcodeperu.admision_academica.application.service.PermisoService;
import com.bootcodeperu.admision_academica.domain.exception.DuplicateResourceException;
import com.bootcodeperu.admision_academica.domain.model.Permiso;
import com.bootcodeperu.admision_academica.domain.repository.PermisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermisoUseCase implements PermisoService {
    private final PermisoRepository permisoRepository;
    private final PermisoMapper permisoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PermisoResponse> getAllPermisos() {
        return permisoRepository.findAll().stream()
                .map(permisoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PermisoResponse createPermiso(PermisoRequest request) {
        if (permisoRepository.existsByNombre(request.nombre())) {
            throw new DuplicateResourceException("Ya existe un permiso con el nombre: " + request.nombre());
        }

        Permiso permiso = new Permiso();
        permiso.setNombre(request.nombre());

        return permisoMapper.toResponse(permisoRepository.save(permiso));
    }
}
