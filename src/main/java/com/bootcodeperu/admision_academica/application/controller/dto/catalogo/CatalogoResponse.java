package com.bootcodeperu.admision_academica.application.controller.dto.catalogo;

import java.util.List;

public record CatalogoResponse(
        Long idUniversidad,
        String nombreUniversidad,
        String siglas,
        List<AreaCatalogoResponse> areas
) {
}