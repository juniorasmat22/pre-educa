package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.token.LogoutRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.token.RefreshTokenRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioRegistroRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    @PostMapping("/registrar")
    public ResponseEntity<ApiResponse<UsuarioResponse>> registrarUsuario(@RequestBody UsuarioRegistroRequest usuario) {
        UsuarioResponse nuevoUsuario = usuarioService.registerUser(usuario);
        return ResponseEntity.ok(ApiResponse.ok(nuevoUsuario, "Usuario creado correctamente"));
    }

    // Ruta para el inicio de sesión (la autenticación real requiere Spring Security JWT/OAuth2)
    // POST /api/v1/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UsuarioResponse>> login(@RequestBody LoginRequest request) {
        // Llama al nuevo servicio de autenticación
        AuthenticationResponse response = authService.authenticate(request);
        // 1. Crear Cookie para el Access Token (Dura 1 hora)
        ResponseCookie jwtCookie = ResponseCookie.from("token", response.getToken())
                .httpOnly(true) // 🔒 INVISIBLE PARA JAVASCRIPT
                .secure(false) // ⚠️ Ponlo en 'true' cuando pases a producción con HTTPS
                .path("/") // Se envía a todas las rutas de la API
                .maxAge(jwtExpiration / 1000) // 1 hora en segundos
                .sameSite("Strict") // Protege contra ataques CSRF
                .build();

        // 2. Crear Cookie para el Refresh Token (Dura 7 días)
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", response.getRefreshToken())
                .httpOnly(true)
                .secure(false) // 'true' en HTTPS
                .path("/api/v1/auth") // 🌟 TRUCO PRO: Esta cookie SOLO viajará cuando llamen a este endpoint exacto
                .maxAge(refreshExpiration / 1000)
                .sameSite("Strict")
                .build();

        // 3. Devolvemos la respuesta SOLO con los datos del usuario, los tokens van ocultos en las cabeceras
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResponse.ok(response.getUsuario(), "Login realizado con exito"));
        //return ResponseEntity.ok(ApiResponse.ok(response, "Login realizado con exito"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @CookieValue(name = "token", required = false) String accessToken,
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {

        // 1. Revocamos los tokens en la base de datos
        authService.logout(accessToken, refreshToken);

        // 2. Creamos cookies "vacías" con tiempo de vida 0 para que el navegador las elimine inmediatamente
        ResponseCookie clearJwtCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false) // Ponlo en true en producción con HTTPS
                .path("/")
                .maxAge(0) // 🧹 ESTO ELIMINA LA COOKIE
                .build();

        ResponseCookie clearRefreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/api/v1/auth") // Debe coincidir con el path donde la creaste
                .maxAge(0)
                .build();

        // 3. Devolvemos la respuesta con las cabeceras de destrucción de cookies
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearJwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, clearRefreshCookie.toString())
                .body(ApiResponse.ok(null, "Sesión cerrada correctamente"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<UsuarioResponse>> refreshToken(
            @CookieValue(name = "refreshToken") String refreshToken) { // Exigimos la cookie

        // 1. Renovamos el token en tu lógica de negocio
        AuthenticationResponse authData = authService.refreshToken(refreshToken);

        // 2. Creamos la NUEVA cookie con el nuevo Access Token
        ResponseCookie jwtCookie = ResponseCookie.from("token", authData.getToken())
                .httpOnly(true)
                .secure(false) // true en HTTPS
                .path("/")
                .maxAge(jwtExpiration / 1000) // 1 hora
                .sameSite("Strict")
                .build();

        // (Opcional) Si en tu authService.refreshToken también rotas el refresh token,
        // deberías crear una cookie nueva para él aquí también. Si devuelves el mismo, no es estrictamente necesario,
        // pero es buena práctica refrescar su tiempo de vida.

        // 3. Devolvemos la respuesta adjuntando la nueva cookie
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(ApiResponse.ok(authData.getUsuario(), "Token renovado correctamente"));
    }

}