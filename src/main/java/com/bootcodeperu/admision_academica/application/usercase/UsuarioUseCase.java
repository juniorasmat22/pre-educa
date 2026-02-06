package com.bootcodeperu.admision_academica.application.usercase;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bootcodeperu.admision_academica.adapter.mapper.UsuarioMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioRegistroRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import com.bootcodeperu.admision_academica.domain.model.Rol;
import com.bootcodeperu.admision_academica.domain.repository.RolRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bootcodeperu.admision_academica.application.service.UsuarioService;
import com.bootcodeperu.admision_academica.domain.exception.DomainValidationException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.Usuario;
import com.bootcodeperu.admision_academica.domain.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioUseCase implements UsuarioService {
	private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    // Debes inyectar el Bean de Spring Security para cifrar contraseñas
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    @Override
    public UsuarioResponse registerUser(UsuarioRegistroRequest request) {
        // 1. Verificar si el email ya existe
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new DomainValidationException(
                    "El email ya está registrado. Por favor, inicia sesión."
            );
        }

        // 2. Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());

        // 3. Hashear la contraseña
        String hashedPassword = passwordEncoder.encode(request.password());
        usuario.setPasswordHash(hashedPassword);


        // 6. Guardar
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario authenticate(String email, String password) {
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

    @Override
    public UsuarioResponse getUserById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    public UsuarioResponse getUserByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    public List<UsuarioResponse> getAllUsers() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponse)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public UsuarioResponse assignRoleToUser(Long userId, Long rolId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado: " + userId));
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Rol no encontrado: " + rolId));
        if (usuario.getRol() != null && usuario.getRol().getId().equals(rolId)) {
            throw new IllegalStateException("El usuario ya tiene asignado este rol");
        }
        usuario.setRol(rol);
        usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuario);
    }

    // =====================================================
    // Métodos de administración
    // =====================================================
    @Override
    @Transactional
    public UsuarioResponse blockUser(Long userId) {
        return setAccountNonLocked(userId, false);
    }

    @Override
    @Transactional
    public UsuarioResponse unblockUser(Long userId) {
        return setAccountNonLocked(userId, true);
    }

    @Override
    @Transactional
    public UsuarioResponse activateUser(Long userId) {
        return setEnabled(userId, true);
    }

    @Override
    @Transactional
    public UsuarioResponse deactivateUser(Long userId) {
        return setEnabled(userId, false);
    }

    @Override
    @Transactional
    public UsuarioResponse changePassword(Long userId, String newPassword) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        usuario.setPasswordHash(passwordEncoder.encode(newPassword));
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    // ==========================
    // Helpers privados
    // ==========================
    private UsuarioResponse setAccountNonLocked(Long userId, boolean nonLocked) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        usuario.setAccountNonLocked(nonLocked);
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    private UsuarioResponse setEnabled(Long userId, boolean enabled) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        usuario.setEnabled(enabled);
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }
}
