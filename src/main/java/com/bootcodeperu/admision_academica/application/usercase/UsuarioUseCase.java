package com.bootcodeperu.admision_academica.application.usercase;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bootcodeperu.admision_academica.application.service.UsuarioService;
import com.bootcodeperu.admision_academica.domain.exception.DomainValidationException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.Usuario;
import com.bootcodeperu.admision_academica.domain.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UsuarioUseCase implements UsuarioService {
	private final UsuarioRepository usuarioRepository;
    // Debes inyectar el Bean de Spring Security para cifrar contraseñas
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        // 1. Verificar si el email ya existe
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new DomainValidationException("El email ya está registrado. Por favor, inicia sesión.");
        }

        // 2. Cifrar la contraseña antes de guardar
        String hashedPassword = passwordEncoder.encode(usuario.getPasswordHash());
        usuario.setPasswordHash(hashedPassword);

        // 3. Guardar en la base de datos
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario autenticar(String email, String password) {
        // 1. Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Credenciales", "email", email)); // Error de autenticación

        // 2. Verificar la contraseña
        // Compara el hash almacenado (usuario.getPasswordHash) con la contraseña plana (password)
        if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
            throw new DomainValidationException("La contraseña es incorrecta."); // Error de autenticación
        }
        
        // Si es correcto, retorna el usuario
        return usuario;
    }
}
