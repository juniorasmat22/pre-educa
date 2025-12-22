package com.bootcodeperu.admision_academica.application.controller.dto.analitica;

public record DebilidadTemaResponse(
        String nombreCurso,
        String nombreTema,
        Double precisionPorcentaje, // Ej: 35% de acierto
        String recomendacion // Ej: "Necesitas reforzar teoría"
) {}