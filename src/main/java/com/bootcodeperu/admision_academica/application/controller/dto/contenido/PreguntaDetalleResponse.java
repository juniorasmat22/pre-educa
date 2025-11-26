package com.bootcodeperu.admision_academica.application.controller.dto.contenido;

import com.fasterxml.jackson.databind.JsonNode;

public record PreguntaDetalleResponse(
	    String id, // ID de MongoDB
	    String enunciado,
	    JsonNode opciones,
	    String fuente,

	    // Campos sensibles para la fase de revisión/corrección:
	    String respuestaCorrecta,
	    JsonNode explicacionDetallada
	) {}
