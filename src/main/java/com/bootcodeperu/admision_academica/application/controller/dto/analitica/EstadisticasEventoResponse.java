package com.bootcodeperu.admision_academica.application.controller.dto.analitica;

public record EstadisticasEventoResponse(
        Long eventoId,
        String tituloEvento,
        Integer totalInscritos,
        Integer totalFinalizados,
        Integer totalAbandonos, // Los que el Scheduler cerró a la fuerza
        Double puntajePromedio,
        Double puntajeMaximo,
        Double puntajeMinimo,
        Double tiempoPromedioMinutos
) {
}
