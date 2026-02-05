package com.bootcodeperu.admision_academica.application.usercase;


import com.bootcodeperu.admision_academica.adapter.mapper.UsuarioMapper;
import org.springframework.stereotype.Service;


import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import com.bootcodeperu.admision_academica.application.service.SeguridadService;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.Rol;
import com.bootcodeperu.admision_academica.domain.model.Usuario;
import com.bootcodeperu.admision_academica.domain.repository.RolRepository;
import com.bootcodeperu.admision_academica.domain.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeguridadUseCase implements SeguridadService {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
   
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