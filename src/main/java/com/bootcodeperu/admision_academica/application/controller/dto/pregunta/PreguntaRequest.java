package com.bootcodeperu.admision_academica.application.controller.dto.pregunta;

import com.bootcodeperu.admision_academica.domain.model.enums.QuestionTarget;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record PreguntaRequest(
        @NotNull Long idTema,
        @NotNull QuestionTarget target,
        @NotBlank String nivel,
        @NotBlank String enunciado,
        String enunciadoImagenUrl,
        @NotNull Map<String, OpcionRequest> opciones, // El Mapa con el feedback por opción
        @NotBlank String respuestaCorrecta,
        String explicacionCorrecta,
        String explicacionIncorrecta,
        String videoExplicativoUrl,
        String fuente,
        List<String> etiquetas,
        Integer anioExamen
) {
}