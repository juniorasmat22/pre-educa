package com.bootcodeperu.admision_academica.application.controller.dto.rol;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record RolRequest(
        @NotBlank(message = "El nombre del rol es obligatorio")
        String nombre,

        // Opcional: IDs de permisos iniciales
        Set<Long> permisosIds
) {}