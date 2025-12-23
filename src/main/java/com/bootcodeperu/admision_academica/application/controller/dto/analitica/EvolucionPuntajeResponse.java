package com.bootcodeperu.admision_academica.application.controller.dto.analitica;

import java.time.LocalDateTime;

public record EvolucionPuntajeResponse(
        LocalDateTime fecha,
        Double puntaje,
        String nombreArea
) {}
