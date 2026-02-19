package com.bootcodeperu.admision_academica.application.controller.dto.simulacro;

import java.time.LocalDateTime;

// Lo que se le devuelve al Frontend para mostrar la tarjeta del evento
public record SimulacroProgramadoResponse(
        Long id,
        String titulo,
        String areaNombre,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        Integer duracionMinutos,
        String estado // Ej: "PROGRAMADO", "EN_CURSO", "FINALIZADO"
) {
}