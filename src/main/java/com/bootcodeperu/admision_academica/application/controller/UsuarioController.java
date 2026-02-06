package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioRegistroRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bootcodeperu.admision_academica.application.controller.dto.auth.AuthenticationResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.auth.LoginRequest;
import com.bootcodeperu.admision_academica.application.service.UsuarioService;
import com.bootcodeperu.admision_academica.application.usercase.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationService authService;
    @PostMapping("/registrar")
    public ResponseEntity<ApiResponse<UsuarioResponse>> registrarUsuario(@RequestBody UsuarioRegistroRequest usuario) {
        UsuarioResponse nuevoUsuario = usuarioService.registerUser(usuario);
        return  ResponseEntity.ok(ApiResponse.ok(nuevoUsuario, "Usuario creado correctamente"));
    }
    
    // Ruta para el inicio de sesión (la autenticación real requiere Spring Security JWT/OAuth2)
    // POST /api/v1/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody LoginRequest request) {
        // Llama al nuevo servicio de autenticación
        AuthenticationResponse response = authService.authenticate(
                request.getEmail(), 
                request.getPassword()
        );
        return ResponseEntity.ok(ApiResponse.ok(response,"Login realizado con exito"));
    }
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        authService.logout(token);
        return ResponseEntity.ok(ApiResponse.ok(null, "Sesión cerrada correctamente"));
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(
            @RequestBody String refreshToken) {
        AuthenticationResponse newToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.ok(newToken, "Token renovado correctamente"));
    }

}