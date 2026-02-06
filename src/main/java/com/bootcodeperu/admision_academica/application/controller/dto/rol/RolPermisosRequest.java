package com.bootcodeperu.admision_academica.application.controller.dto.rol;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record RolPermisosRequest(
        @NotEmpty(message = "Debe enviar al menos un permiso")
        Set<Long> permisosIds
) {}