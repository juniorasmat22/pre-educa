package com.bootcodeperu.admision_academica.application.controller.dto.curso_area;

public record CursoAreaSimpleResponse(
        Long areaId,
        String areaNombre,
        Long cursoId,
        String cursoNombre,
        Integer cantidadPreguntas
) {
}
