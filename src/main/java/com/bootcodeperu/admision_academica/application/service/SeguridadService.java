package com.bootcodeperu.admision_academica.application.service;

import java.util.List;

import com.bootcodeperu.admision_academica.domain.model.Permiso;
import com.bootcodeperu.admision_academica.domain.model.Rol;
import com.bootcodeperu.admision_academica.domain.model.Usuario;

public interface SeguridadService {
    
    // Roles
    Rol saveRol(Rol rol);
    List<Rol> findAllRoles();
    
    // Permisos
    Permiso savePermiso(Permiso permiso);
    List<Permiso> findAllPermisos();
    
    /**
     * Asigna un Rol existente a un Usuario.
     * @param userId ID del usuario.
     * @param rolName Nombre del rol a asignar (Ej: ROLE_ESTUDIANTE).
     * @return Usuario actualizado.
     */
    Usuario asignarRolAUsuario(Long userId, String rolName);
}