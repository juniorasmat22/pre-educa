package com.bootcodeperu.admision_academica.application.controller.dto.procesoadmision;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ProcesoAdmisionRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotNull(message = "La fecha del examen es obligatoria") LocalDate fechaExamen,
        @NotNull(message = "El estado vigente es obligatorio") Boolean vigente,
        @NotNull(message = "La universidad es obligatoria") Long idUniversidad
) {
}