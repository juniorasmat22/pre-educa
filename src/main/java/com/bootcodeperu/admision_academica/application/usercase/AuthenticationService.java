package com.bootcodeperu.admision_academica.application.usercase;

import java.util.List;

import com.bootcodeperu.admision_academica.adapter.security.UsuarioPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.bootcodeperu.admision_academica.application.controller.dto.auth.AuthenticationResponse;
import com.bootcodeperu.admision_academica.domain.model.Token;
import com.bootcodeperu.admision_academica.domain.model.Usuario;
import com.bootcodeperu.admision_academica.domain.repository.TokenRepository;
import com.bootcodeperu.admision_academica.domain.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional // Asegura que la DB se actualice correctamente
    public AuthenticationResponse authenticate(String email, String password) {
        
        // 1. Autenticar el usuario usando Spring Security
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // 2. Obtener el objeto Usuario completo
//        Usuario usuario = usuarioRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado después de la autenticación."));
        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.usuario();
        // 3. Generar el Token JWT
        String jwt = jwtService.generateToken(principal);

        // 4. Revocar todos los tokens anteriores del usuario (mejor práctica de seguridad)
        revokeAllUserTokens(usuario);

        // 5. Guardar el nuevo token
        saveUserToken(usuario, jwt);

        // 6. Devolver la respuesta con el token
        return AuthenticationResponse.builder()
                .token(jwt)
                .usuario(usuario)
                .build();
    }
    
    // --- Métodos de apoyo para persistencia de tokens ---

    private void saveUserToken(Usuario usuario, String jwtToken) {
        Token token = new Token();
        token.setUsuario(usuario);
        token.setToken(jwtToken);
        token.setTipoToken("BEARER");
        token.setExpirado(false);
        token.setRevocado(false);
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