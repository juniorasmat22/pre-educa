package com.bootcodeperu.admision_academica.application.controller.dto.pregunta;

import jakarta.validation.constraints.NotBlank;

public record OpcionRequest(
        @NotBlank String texto,
        @NotBlank String feedback
) {
}