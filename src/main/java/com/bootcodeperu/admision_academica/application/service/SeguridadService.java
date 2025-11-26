package com.bootcodeperu.admision_academica.application.service;

import java.util.List;

import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import com.bootcodeperu.admision_academica.domain.model.Permiso;
import com.bootcodeperu.admision_academica.domain.model.Rol;

public interface SeguridadService {
    
    // Roles
    Rol saveRol(Rol rol);
    List<RolResponse> findAllRoles();
    
    // Permisos
    Permiso savePermiso(Permiso permiso);
    List<PermisoResponse> findAllPermisos();
    
    /**
     * Asigna un Rol existente a un Usuario.
     * @param userId ID del usuario.
     * @param rolName Nombre del rol a asignar (Ej: ROLE_ESTUDIANTE).
     * @return Usuario actualizado.
     */
    UsuarioResponse asignarRolAUsuario(Long userId, String rolName);
}