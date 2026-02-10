package com.bootcodeperu.admision_academica.application.controller.dto.pregunta;

import com.bootcodeperu.admision_academica.domain.model.enums.QuestionTarget;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record PreguntaRequest(@NotNull Long idTema,
                              @NotNull QuestionTarget target,
                              @NotBlank String nivel,
                              @NotBlank String enunciado,
                              @NotNull Map<String, Object> opciones, // Null si es Flashcard
                              @NotBlank String respuestaCorrecta, // Reverso si es Flashcard
                              String explicacionCorrecta,
                              String explicacionIncorrecta,
                              Integer anioExamen
) {
}