package com.bootcodeperu.admision_academica.application.controller.dto.objetivo;

import jakarta.validation.constraints.NotNull;

public record ObjetivoAcademicoRequest(
        @NotNull Long idUsuario,
        @NotNull Long idCarrera,
        Long idProcesoAdmision,
        @NotNull Boolean postulaProximoExamen,
        @NotNull Boolean objetivoPrincipal,
        @NotNull Boolean activo
) {
}