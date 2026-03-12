package com.bootcodeperu.admision_academica.application.controller.dto.catalogo;

import java.util.List;

public record AreaCatalogoResponse(
        Long idArea,
        String nombreArea,
        List<CarreraCatalogoResponse> carreras
) {
}
