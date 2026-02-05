package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcodeperu.admision_academica.application.controller.dto.auth.AuthenticationResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.auth.LoginRequest;
import com.bootcodeperu.admision_academica.application.service.UsuarioService;
import com.bootcodeperu.admision_academica.application.usercase.AuthenticationService;
import com.bootcodeperu.admision_academica.domain.model.Usuario;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationService authService;
    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody Usuario usuario) {
        // NOTA: Se recomienda usar un DTO (Data Transfer Object) para la entrada
        Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
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
}