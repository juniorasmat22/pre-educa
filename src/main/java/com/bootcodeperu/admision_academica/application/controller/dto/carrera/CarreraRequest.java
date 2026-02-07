package com.bootcodeperu.admision_academica.application.controller.dto.carrera;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CarreraRequest(
        @NotBlank(message = "El nombre de la carrera es obligatorio")
        String nombre,

        @NotNull(message = "Se requiere el ID de área")
        Long areaId
) {}