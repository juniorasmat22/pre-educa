package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.mapper.RolMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolResponse;
import com.bootcodeperu.admision_academica.application.service.RolService;
import com.bootcodeperu.admision_academica.domain.exception.DuplicateResourceException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.Permiso;
import com.bootcodeperu.admision_academica.domain.model.Rol;
import com.bootcodeperu.admision_academica.domain.repository.PermisoRepository;
import com.bootcodeperu.admision_academica.domain.repository.RolRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RolUseCase implements RolService {
    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final RolMapper rolMapper;
    @Override
    @Transactional(readOnly = true)
    public List<RolResponse> getAllRoles() {
        return rolRepository.findAll().stream()
                .map(rolMapper::toRolResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RolResponse createRol(RolRequest request) {
        if (rolRepository.existsByNombre(request.nombre())) {
            throw new DuplicateResourceException("Ya existe un rol con el nombre: " + request.nombre());
        }

        Rol rol = new Rol();
        rol.setNombre(request.nombre());

        // Asignar permisos si vienen en el request
        if (request.permisosIds() != null && !request.permisosIds().isEmpty()) {
            List<Permiso> permisos = permisoRepository.findAllById(request.permisosIds());
            if (permisos.size() != request.permisosIds().size()) {
                throw new ResourceNotFoundException("Algunos permisos no fueron encontrados");
            }
            rol.setPermisos(new HashSet<>(permisos));
        }

        return rolMapper.toRolResponse(rolRepository.save(rol));
    }
}
