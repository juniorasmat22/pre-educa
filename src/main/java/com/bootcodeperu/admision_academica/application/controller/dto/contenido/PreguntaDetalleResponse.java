package com.bootcodeperu.admision_academica.application.controller.dto.contenido;


import com.bootcodeperu.admision_academica.application.controller.dto.pregunta.OpcionDetalleDTO;

import java.util.Map;

public record PreguntaDetalleResponse(
        String id,
        String enunciado,
        String enunciadoImagenUrl,
        Map<String, OpcionDetalleDTO> opciones, // Texto + Feedback por opción
        String respuestaCorrecta,
        String explicacionCorrecta,
        String explicacionIncorrecta,
        String videoExplicativoUrl,
        String fuente
) {
}