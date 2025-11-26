package com.bootcodeperu.admision_academica.application.controller.dto.rol;

import java.util.Set;

import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoResponse;

public record RolResponse (Long id, String nombre,Set<PermisoResponse> permisos) {
	
}
