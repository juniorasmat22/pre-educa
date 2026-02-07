package com.bootcodeperu.admision_academica.application.controller.dto.contenido;

import java.util.List;

public record ContenidoTeoriaRequest(
        String titulo,
        List<SeccionContenidoResponse> secciones
) {}