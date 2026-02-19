package com.bootcodeperu.admision_academica.application.controller.dto.simulacro;

import java.time.LocalDateTime;

public record CrearSimulacroProgramadoRequest(
        String titulo,
        Long areaId,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin
) {
}