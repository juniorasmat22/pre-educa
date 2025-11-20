package com.bootcodeperu.admision_academica.application.controller.dto.area;

public record AreaResponse(
	    Long id,
	    String nombre,
	    Double puntajeCorrecta,
	    Double puntajeIncorrecta
	) {}