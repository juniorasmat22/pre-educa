package com.bootcodeperu.admision_academica.application.controller.dto.progreso;

public record ProgresoUpdateRequest(
	    Long temaId,
	    Double nuevoPuntaje // Puntaje obtenido en la última práctica
	) {}