package com.bootcodeperu.admision_academica.application.controller.dto.analitica;

public record RecomendacionResponse(
        String prioridad, // 'ALTA', 'MEDIA', 'MANTENIMIENTO'
        String titulo,
        String mensaje,
        Long temaId,
        String cursoNombre
) {}