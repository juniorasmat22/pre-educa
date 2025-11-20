package com.bootcodeperu.admision_academica.application.controller.dto.usuario;

public record UsuarioRegistroRequest(
	    String nombre,
	    String email,
	    String password, // Contraseña en texto plano
	    Long idAreaPostulacion
	    // Long idCarreraDeseada, si hubieras mantenido ese modelo
	) {}