package com.bootcodeperu.admision_academica.application.controller.dto.flashcard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FlashcardRequest(
        @NotNull Long idTema,
        @NotBlank String nivel,
        @NotBlank String frente,
        @NotBlank String dorso,
        String imagenUrl
) {
}