package com.bootcodeperu.admision_academica.application.controller.dto.analitica;

public record RankingUsuarioResponse(
        Integer posicion,
        String nombreUsuario,
        Double puntajeMaximo,
        String carreraDeseada
) {}