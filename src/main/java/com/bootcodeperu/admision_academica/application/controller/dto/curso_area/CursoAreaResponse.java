package com.bootcodeperu.admision_academica.application.controller.dto.curso_area;

import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso.CursoResponse;

public record CursoAreaResponse(
        AreaResponse area,
        CursoResponse curso,
        Integer cantidadPreguntas
) {
}
