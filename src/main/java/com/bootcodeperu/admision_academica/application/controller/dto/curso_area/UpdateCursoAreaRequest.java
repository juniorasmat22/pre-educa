package com.bootcodeperu.admision_academica.application.controller.dto.curso_area;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCursoAreaRequest(

        @NotNull(message = "La cantidad de preguntas es obligatoria")
        @Min(value = 0, message = "La cantidad de preguntas no puede ser negativa")
        Integer cantidadPreguntas
) {
}