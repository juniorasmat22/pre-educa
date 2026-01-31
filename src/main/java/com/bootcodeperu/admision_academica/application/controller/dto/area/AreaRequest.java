package com.bootcodeperu.admision_academica.application.controller.dto.area;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AreaRequest(

        @NotBlank(message = "El nombre del área es obligatorio")
        String nombre,

        @NotBlank(message = "La descripción del área es obligatoria")
        String descripcion,

        @NotNull(message = "El puntaje por respuesta correcta es obligatorio")
        Double puntajeCorrecta,

        @NotNull(message = "El puntaje por respuesta incorrecta es obligatorio")
        Double puntajeIncorrecta,

        @NotNull(message = "El puntaje por respuesta en blanco es obligatorio")
        Double puntajeBlanco
) {}
