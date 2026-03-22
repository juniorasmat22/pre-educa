package com.bootcodeperu.admision_academica.application.usercase;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import com.bootcodeperu.admision_academica.adapter.mapper.ObjetivoAcademicoMapper;
import com.bootcodeperu.admision_academica.adapter.mapper.UsuarioMapper;
import com.bootcodeperu.admision_academica.adapter.security.UsuarioPrincipal;
import com.bootcodeperu.admision_academica.application.controller.dto.auth.LoginRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.objetivo.ObjetivoAcademicoResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import com.bootcodeperu.admision_academica.domain.exception.DomainValidationException;
import com.bootcodeperu.admision_academica.domain.exception.InvalidTokenException;
import com.bootcodeperu.admision_academica.domain.model.enums.Plataforma;
import com.bootcodeperu.admision_academica.domain.model.enums.TipoToken;
import com.bootcodeperu.admision_academica.domain.repository.ObjetivoAcademicoRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.bootcodeperu.admision_academica.application.controller.dto.auth.AuthenticationResponse;
import com.bootcodeperu.admision_academica.domain.model.Token;
import com.bootcodeperu.admision_academica.domain.model.Usuario;
import com.bootcodeperu.admision_academica.domain.repository.TokenRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioMapper usuarioMapper;
    private final ObjetivoAcademicoRepository objetivoRepository;
    private final ObjetivoAcademicoMapper objetivoMapper;

    @Transactional // Asegura que la DB se actualice correctamente
    public AuthenticationResponse authenticate(LoginRequest request) {

        // 1. Autenticar el usuario usando Spring Security
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Obtener el objeto Usuario completo
//        Usuario usuario = usuarioRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado después de la autenticación."));
        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.usuario();
        if (principal.usuario().getRol() == null) {
            throw new DomainValidationException("El usuario no tiene un rol asignado");
        }
        //3. BUSCAMOS SU OBJETIVO ACADÉMICO (Si no tiene, será null)
        ObjetivoAcademicoResponse objetivo = objetivoRepository
                .findByUsuarioIdAndObjetivoPrincipalTrueAndActivoTrue(usuario.getId())
                .map(objetivoMapper::toResponse)
                .orElse(null); // Si es Admin/Profesor, esto será NULL automáticamente
        UsuarioResponse usuarioResponse = usuarioMapper.toResponseConObjetivo(usuario, objetivo);
        // 4. Generar el Token JWT
        String jwt = jwtService.generateToken(principal);
        String jwtRefreshToken = jwtService.generateRefreshToken(principal);
        // 4. Revocar todos los tokens anteriores del usuario
        //revokeAllUserTokens(usuario);
        Plataforma origen = request.getPlataforma() != null ? request.getPlataforma() : Plataforma.DESCONOCIDA;
        if (origen != Plataforma.DESCONOCIDA) {
            List<Token> sesionesAnteriores = tokenRepository.encontrarTokensVivosPorPlataforma(usuario.getId(), origen);
            if (!sesionesAnteriores.isEmpty()) {
                sesionesAnteriores.forEach(t -> {
                    t.setRevocado(true);
                    t.setExpirado(true);
                });
                tokenRepository.saveAll(sesionesAnteriores);
            }
        }
        // 5. Guardar el nuevo token
        saveUserToken(usuario, jwt, TipoToken.ACCESS, origen);
        saveUserToken(usuario, jwtRefreshToken, TipoToken.REFRESH, origen);
        // 6. Devolver la respuesta con el token
        return AuthenticationResponse.builder()
                .token(jwt)
                .refreshToken(jwtRefreshToken)
                .usuario(usuarioResponse)
                .build();
    }

    // --- Cerrar sesión ---
    @Transactional
    public void logout(String accessToken, String refreshToken) {
        // 1. Revocar el Access Token del dispositivo actual
        if (accessToken != null) {
            tokenRepository.findByToken(accessToken).ifPresent(t -> {
                t.setRevocado(true);
                t.setExpirado(true);
                tokenRepository.save(t);
            });
        }

        // 2. Revocar el Refresh Token del dispositivo actual
        // Esto evita que cierren la sesión de la App Móvil si el usuario le dio "Salir" en la Web.
        if (refreshToken != null) {
            tokenRepository.findByToken(refreshToken).ifPresent(t -> {
                t.setRevocado(true);
                t.setExpirado(true);
                tokenRepository.save(t);
            });
        }
    }

    // ==========================================
    // 🔄 REFRESCAR TOKEN (MANTENIENDO LA PLATAFORMA)
    // ==========================================
    @Transactional
    public AuthenticationResponse refreshToken(String refreshToken) {

        // 1. Validar la criptografía del JWT
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new InvalidTokenException("Refresh token inválido o expirado");
        }

        // 2. Validar contra la Base de Datos (Seguridad Estricta)
        Token storedRefreshToken = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidTokenException("Refresh token no encontrado en la base de datos"));

        if (storedRefreshToken.isRevocado() || storedRefreshToken.isExpirado()) {
            throw new InvalidTokenException("La sesión ha caducado o fue cerrada. Inicie sesión nuevamente.");
        }

        // Asegurarnos de que no nos envíen un Access Token para intentar refrescar
        if (storedRefreshToken.getTipoToken() != TipoToken.REFRESH) {
            throw new InvalidTokenException("El token proporcionado no es válido para esta operación.");
        }

        // 3. Obtener Usuario y la Plataforma desde la que se inició esta sesión originalmente
        Usuario usuario = storedRefreshToken.getUsuario();
        Plataforma plataformaOrigen = storedRefreshToken.getPlataforma();
        UsuarioPrincipal principal = new UsuarioPrincipal(usuario);
        List<Token> tokensBasura = tokenRepository.encontrarTokensVencidosPorUsuario(usuario.getId(), LocalDateTime.now());
        if (!tokensBasura.isEmpty()) {
            tokensBasura.forEach(t -> t.setExpirado(true)); // O podrías usar tokenRepository.deleteAll(tokensBasura);
            tokenRepository.saveAll(tokensBasura);
        }

        // 4. Generar NUEVO Access Token
        String newAccessToken = jwtService.generateToken(principal);

        // 5. Guardar el nuevo token asignándole la MISMA plataforma del Refresh Token
        // Así no perdemos el rastro de dónde viene el usuario.
        saveUserToken(usuario, newAccessToken, TipoToken.ACCESS, plataformaOrigen);

        // 6. Devolver respuesta
        return AuthenticationResponse.builder()
                .token(newAccessToken)
                .refreshToken(refreshToken) // Mantenemos el mismo refresh token vivo
                .usuario(usuarioMapper.toResponse(usuario))
                .build();
    }
    // --- Métodos de apoyo para persistencia de tokens ---

    private void saveUserToken(Usuario usuario, String jwtToken, TipoToken tipo, Plataforma plataforma) {
        Token token = new Token();
        token.setUsuario(usuario);
        token.setToken(jwtToken);
        token.setTipoToken(tipo);
        token.setPlataforma(plataforma);
        token.setExpirado(false);
        token.setRevocado(false);
        java.util.Date expirationDate = jwtService.extractExpiration(jwtToken);
        LocalDateTime fechaExp = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        token.setFechaExpiracion(fechaExp);
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Usuario usuario) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(usuario.getId());
        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(t -> {
            t.setExpirado(true);
            t.setRevocado(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}