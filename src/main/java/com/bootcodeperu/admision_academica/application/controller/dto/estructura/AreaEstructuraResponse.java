package com.bootcodeperu.admision_academica.application.controller.dto.estructura;

import java.util.List;

public record AreaEstructuraResponse(
        Long idArea,
        String nombreArea,
        List<CarreraEstructuraResponse> carreras
) {
}
