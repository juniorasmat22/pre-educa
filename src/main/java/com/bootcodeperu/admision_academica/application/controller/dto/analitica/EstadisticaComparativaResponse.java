package com.bootcodeperu.admision_academica.application.controller.dto.analitica;

public record EstadisticaComparativaResponse(
        Double puntajeObtenido,
        Double promedioArea,
        Double percentil, // El valor del 0 al 100
        String mensajeInterpretativo // Ej: "Estás en el top 20% de tu área"
) {}