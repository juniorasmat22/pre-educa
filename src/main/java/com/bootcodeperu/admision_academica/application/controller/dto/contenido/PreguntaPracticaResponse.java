package com.bootcodeperu.admision_academica.application.controller.dto.contenido;

import com.bootcodeperu.admision_academica.application.controller.dto.pregunta.OpcionPracticaDTO;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public record PreguntaPracticaResponse(
        String id,
        String enunciado,
        String enunciadoImagenUrl,
        Map<String, OpcionPracticaDTO> opciones, // Solo texto, sin feedback
        String fuente
) {
}
