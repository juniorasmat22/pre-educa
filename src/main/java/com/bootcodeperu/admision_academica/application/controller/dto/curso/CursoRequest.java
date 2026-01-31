package com.bootcodeperu.admision_academica.application.controller.dto.curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CursoRequest(
        @NotBlank(message = "El nombre del curso es obligatorio")
        @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres")
        String nombre,

        @Size(max = 5000, message = "La descripción no puede exceder los 5000 caracteres")
        String descripcion
) {
}
