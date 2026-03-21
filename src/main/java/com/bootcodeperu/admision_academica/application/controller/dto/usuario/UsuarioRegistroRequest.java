package com.bootcodeperu.admision_academica.application.controller.dto.usuario;

import jakarta.validation.constraints.NotNull;

public record UsuarioRegistroRequest(
        String nombre,
        String email,
        String password, // Contraseña en texto plano
        @NotNull(message = "Debes seleccionar una carrera")
        Long idCarrera,
        Long idProcesoAdmision // Opcional (puede ser null)
) {
}