package com.bootcodeperu.admision_academica.application.controller.dto.contenido;


import java.util.Map;

public record PreguntaDetalleResponse(
        String id, // ID de MongoDB
        String enunciado,
        Map<String, Object> opciones,
        String fuente,

        // Campos sensibles para la fase de revisión/corrección:
        String respuestaCorrecta,
        Map<String, Object> explicacionDetallada
) {
}
