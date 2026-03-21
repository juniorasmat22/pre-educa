package com.bootcodeperu.admision_academica.application.controller.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioAdminRequest(
        @NotBlank String nombre,
        @NotBlank String email,
        @NotBlank String password,
        @NotNull Long idRol
) {
}