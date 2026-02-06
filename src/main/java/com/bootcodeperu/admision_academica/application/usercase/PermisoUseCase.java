package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.mapper.PermisoMapper;
import com.bootcodeperu.admision_academica.adapter.mapper.RolMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolResponseMini;
import com.bootcodeperu.admision_academica.application.service.PermisoService;
import com.bootcodeperu.admision_academica.domain.exception.DuplicateResourceException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.Permiso;
import com.bootcodeperu.admision_academica.domain.repository.PermisoRepository;
import com.bootcodeperu.admision_academica.domain.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermisoUseCase implements PermisoService {
    private final PermisoRepository permisoRepository;
    private final PermisoMapper permisoMapper;
    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

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

    @Override
    public PermisoResponse updatePermiso(Long id, PermisoRequest request) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso no encontrado con ID: " + id));

        if (!permiso.getNombre().equals(request.nombre()) && permisoRepository.existsByNombre(request.nombre())) {
            throw new DuplicateResourceException("Ya existe un permiso con el nombre: " + request.nombre());
        }

        permiso.setNombre(request.nombre());
        permisoRepository.save(permiso);

        return permisoMapper.toResponse(permiso);
    }

    @Override
    public void deletePermiso(Long id) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso no encontrado con ID: " + id));
        permisoRepository.delete(permiso);
    }

    @Override
    @Transactional(readOnly = true)
    public PermisoResponse getPermisoById(Long id) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso no encontrado con ID: " + id));
        return permisoMapper.toResponse(permiso);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<RolResponseMini> getRolesByPermiso(Long id) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso no encontrado con ID: " + id));

        return rolRepository.findAllByPermisosContaining(permiso)
                .stream()
                .map(rolMapper::toRolResponseMini)
                .collect(Collectors.toSet());
    }
}
