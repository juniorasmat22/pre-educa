package com.bootcodeperu.admision_academica.application.usercase;

import java.util.List;
import java.util.stream.Collectors;

import com.bootcodeperu.admision_academica.adapter.mapper.SeguridadMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import com.bootcodeperu.admision_academica.application.service.SeguridadService;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.Permiso;
import com.bootcodeperu.admision_academica.domain.model.Rol;
import com.bootcodeperu.admision_academica.domain.model.Usuario;
import com.bootcodeperu.admision_academica.domain.repository.PermisoRepository;
import com.bootcodeperu.admision_academica.domain.repository.RolRepository;
import com.bootcodeperu.admision_academica.domain.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeguridadUseCase implements SeguridadService {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final SeguridadMapper seguridadMapper;
    @Override
    public Rol saveRol(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public List<RolResponse> findAllRoles() {
        return rolRepository.findAll().stream()
                .map(seguridadMapper::toRolResponse) // Uso limpio y eficiente
                .collect(Collectors.toList());
    }

    @Override
    public Permiso savePermiso(Permiso permiso) {
        return permisoRepository.save(permiso);
    }

    @Override
    public List<PermisoResponse> findAllPermisos() {
    	return permisoRepository.findAll().stream()
                .map(seguridadMapper::toPermisoResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public UsuarioResponse asignarRolAUsuario(Long userId, String rolName) {
        // 1. Obtener Usuario
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));

        // 2. Obtener Rol
        Rol rol = rolRepository.findByNombre(rolName)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "nombre", rolName));

        // 3. Asignar y Guardar
        usuario.setRol(rol);
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        
        // 4. Devolver DTO
        return modelMapper.map(usuarioActualizado, UsuarioResponse.class);
    }
}