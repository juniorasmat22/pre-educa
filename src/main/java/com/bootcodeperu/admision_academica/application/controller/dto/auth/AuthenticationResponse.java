package com.bootcodeperu.admision_academica.application.controller.dto.auth;

import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import com.bootcodeperu.admision_academica.domain.model.Usuario;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private String token;
    private UsuarioResponse usuario; // O podrías usar un DTO simple de usuario
}
