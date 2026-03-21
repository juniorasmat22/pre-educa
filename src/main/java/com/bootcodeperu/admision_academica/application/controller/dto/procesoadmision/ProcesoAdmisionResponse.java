package com.bootcodeperu.admision_academica.application.controller.dto.procesoadmision;

import java.time.LocalDate;

public record ProcesoAdmisionResponse(
        Long id,
        String nombre,
        LocalDate fechaExamen,
        boolean vigente,
        Long idUniversidad,
        String nombreUniversidad
) {
}