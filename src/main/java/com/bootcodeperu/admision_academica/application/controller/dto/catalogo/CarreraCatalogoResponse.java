package com.bootcodeperu.admision_academica.application.controller.dto.catalogo;

public record CarreraCatalogoResponse(
        Long idCarrera,
        String nombreCarrera,
        Double puntajeMinimoHistorico
) {
}
