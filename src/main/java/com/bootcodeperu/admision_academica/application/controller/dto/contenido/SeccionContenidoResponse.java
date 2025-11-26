package com.bootcodeperu.admision_academica.application.controller.dto.contenido;

public record SeccionContenidoResponse(
	    Integer orden,
	    String tipo,     // 'VIDEO', 'TEXTO', 'IMAGEN'
	    String subtitulo,
	    String texto,
	    String url
	) {}