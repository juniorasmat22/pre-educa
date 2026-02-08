package com.bootcodeperu.admision_academica.application.controller.dto.contenido;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public record PreguntaPracticaResponse(
        String id,
        String enunciado,
        Map<String, Object> opciones,
        String fuente
) {
}
