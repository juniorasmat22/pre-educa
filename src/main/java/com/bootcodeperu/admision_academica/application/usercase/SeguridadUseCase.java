package com.bootcodeperu.admision_academica.application.usercase;

import java.util.List;
import java.util.stream.Collectors;

import com.bootcodeperu.admision_academica.adapter.mapper.PermisoMapper;
import com.bootcodeperu.admision_academica.adapter.mapper.RolMapper;
import com.bootcodeperu.admision_academica.adapter.mapper.UsuarioMapper;
import com.bootcodeperu.admision_academica.domain.exception.DuplicateResourceException;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final RolMapper rolMapper;
    private final PermisoMapper permisoMapper;
    private final UsuarioMapper usuarioMapper;
    @Override
    public RolResponse saveRol(Rol rol) {
        if (rol.getNombre() == null || rol.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del rol no puede estar vacío");
        }
        Rol rolGuardado;
        try {
            rolGuardado = rolRepository.save(rol);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Ya existe un rol con ese nombre", e);
        }
        return rolMapper.toRolResponse(rolGuardado);
    }

    @Override
    public List<RolResponse> findAllRoles() {
        return rolRepository.findAll().stream()
                .map(rolMapper::toRolResponse) // Uso limpio y eficiente
                .collect(Collectors.toList());
    }

    @Override
    public PermisoResponse savePermiso(Permiso permiso) {
        if (permiso.getNombre() == null || permiso.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del permiso no puede estar vacío");
        }
        try {
            Permiso permissionGuardado = permisoRepository.save(permiso);
            return permisoMapper.toResponse(permissionGuardado);
        } catch (DataIntegrityViolationException e) {
            // Lanza nuestra excepción genérica de duplicado
            throw new DuplicateResourceException("Ya existe un permiso con ese nombre", e);
        }
    }

    @Override
    public List<PermisoResponse> findAllPermisos() {
    	return permisoRepository.findAll().stream()
                .map(permisoMapper::toResponse)
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
        return usuarioMapper.toResponse(usuarioActualizado);
    }
}