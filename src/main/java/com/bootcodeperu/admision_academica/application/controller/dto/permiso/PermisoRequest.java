package com.bootcodeperu.admision_academica.application.controller.dto.permiso;

import jakarta.validation.constraints.NotBlank;

public record PermisoRequest(
        @NotBlank(message = "El nombre del permiso es obligatorio")
        String nombre,

        String descripcion
) {}