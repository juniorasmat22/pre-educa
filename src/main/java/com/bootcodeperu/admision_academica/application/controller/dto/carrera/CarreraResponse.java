package com.bootcodeperu.admision_academica.application.controller.dto.carrera;

public record CarreraResponse(
        Long id,
        String nombre,
        Long areaId,
        String areaNombre
) {}