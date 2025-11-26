package com.bootcodeperu.admision_academica.application.controller.dto.contenido;

import java.time.LocalDateTime;
import java.util.List;

public record ContenidoTeoriaResponse(
	    String id, // ID de MongoDB
	    String titulo,
	    List<SeccionContenidoResponse> secciones,
	    LocalDateTime ultimaActualizacion
	) {}