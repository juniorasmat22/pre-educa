package com.bootcodeperu.admision_academica.application.controller.dto.tema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TemaRequest(
        @NotNull(message = "El ID del curso es obligatorio")
        Long cursoId,

        @NotBlank(message = "El nombre del tema no puede estar vacío")
        String nombreTema
) {}