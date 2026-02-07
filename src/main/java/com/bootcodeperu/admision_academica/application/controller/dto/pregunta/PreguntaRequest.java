package com.bootcodeperu.admision_academica.application.controller.dto.pregunta;

import com.bootcodeperu.admision_academica.domain.model.QuestionTarget;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PreguntaRequest(@NotNull Long idTema,
                              @NotNull QuestionTarget target,
                              @NotBlank String nivel,
                              @NotBlank String enunciado,
                              @NotBlank JsonNode opciones, // Null si es Flashcard
                              @NotBlank String respuestaCorrecta, // Reverso si es Flashcard
                              String explicacionCorrecta,
                              String explicacionIncorrecta,
                              Integer anioExamen
) {
}