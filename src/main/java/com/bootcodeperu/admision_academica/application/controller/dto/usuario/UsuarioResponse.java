package com.bootcodeperu.admision_academica.application.controller.dto.usuario;

import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolResponse;

public record UsuarioResponse(
	    Long id,
	    String nombre,
	    String email,
	    String carreraDeseada,
	    RolResponse rol // Usamos un DTO para el Rol también
	) {}