package com.bootcodeperu.admision_academica.application.controller.dto.curso_area;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateCursoAreaRequest(

        @NotNull(message = "El id del área es obligatorio")
        Long areaId,

        @NotNull(message = "El id del curso es obligatorio")
        Long cursoId,

        @NotNull(message = "La cantidad de preguntas es obligatoria")
        @Min(value = 0, message = "La cantidad de preguntas no puede ser negativa")
        Integer cantidadPreguntas
) {
}