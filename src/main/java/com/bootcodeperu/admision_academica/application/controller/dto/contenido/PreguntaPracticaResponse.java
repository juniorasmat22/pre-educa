package com.bootcodeperu.admision_academica.application.controller.dto.contenido;

import com.fasterxml.jackson.databind.JsonNode;

public record PreguntaPracticaResponse(
        String id,
        String enunciado,
        JsonNode opciones,
        String fuente
) {}
