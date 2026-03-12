package com.bootcodeperu.admision_academica.application.controller.dto.universidad;

public record UniversidadResponse(
        Long id,
        String nombre,
        String siglas,
        boolean activo
) {
}