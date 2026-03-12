package com.bootcodeperu.admision_academica.application.controller.dto.estructura;

public record CarreraEstructuraResponse(
        Long idCarrera,
        String nombreCarrera,
        Double puntajeMinimoHistorico
) {
}
