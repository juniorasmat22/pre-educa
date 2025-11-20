package com.bootcodeperu.admision_academica.application.usercase;

import java.util.List;

import org.springframework.stereotype.Service;

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

    @Override
    public Rol saveRol(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public List<Rol> findAllRoles() {
        return rolRepository.findAll();
    }

    @Override
    public Permiso savePermiso(Permiso permiso) {
        return permisoRepository.save(permiso);
    }

    @Override
    public List<Permiso> findAllPermisos() {
        return permisoRepository.findAll();
    }
    
    @Override
    @Transactional
    public Usuario asignarRolAUsuario(Long userId, String rolName) {
        // 1. Obtener Usuario
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));

        // 2. Obtener Rol
        Rol rol = rolRepository.findByNombre(rolName)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "nombre", rolName));

        // 3. Asignar y Guardar
        usuario.setRol(rol);
        return usuarioRepository.save(usuario);
    }
}