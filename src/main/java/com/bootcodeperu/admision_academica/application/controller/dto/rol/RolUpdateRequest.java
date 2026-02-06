package com.bootcodeperu.admision_academica.application.controller.dto.rol;

import jakarta.validation.constraints.NotBlank;

public record RolUpdateRequest(
        @NotBlank(message = "El nombre del rol es obligatorio")
        String nombre
) {}