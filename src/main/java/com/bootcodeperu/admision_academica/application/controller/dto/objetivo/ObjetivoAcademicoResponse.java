package com.bootcodeperu.admision_academica.application.controller.dto.objetivo;

public record ObjetivoAcademicoResponse(
        Long id,
        Long idUsuario,
        String nombreUsuario,
        Long idCarrera,
        String nombreCarrera,
        Long idProcesoAdmision,
        String nombreProceso,
        boolean postulaProximoExamen,
        boolean objetivoPrincipal,
        boolean activo
) {
}