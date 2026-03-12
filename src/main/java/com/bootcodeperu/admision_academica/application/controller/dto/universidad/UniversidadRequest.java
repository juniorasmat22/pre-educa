package com.bootcodeperu.admision_academica.application.controller.dto.universidad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UniversidadRequest(
        @NotBlank(message = "El nombre de la universidad es obligatorio")
        String nombre,

        @NotBlank(message = "Las siglas son obligatorias")
        String siglas
) {
}