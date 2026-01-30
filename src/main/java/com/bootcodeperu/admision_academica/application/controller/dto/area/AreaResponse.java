package com.bootcodeperu.admision_academica.application.controller.dto.area;

public record AreaResponse(
	    Long id,
	    String nombre,
		String descripcion,
	    Double puntajeCorrecta,
	    Double puntajeIncorrecta,
		Double puntajeBlanco
	) {}