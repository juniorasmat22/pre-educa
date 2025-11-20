package com.bootcodeperu.admision_academica.application.service;

import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.Usuario;

public interface UsuarioService {
	/**
     * Registra un nuevo usuario, incluyendo el hash de la contraseña.
     * @param usuario El objeto Usuario a registrar.
     * @return El Usuario creado.
     */
    Usuario registrarUsuario(Usuario usuario);

    /**
     * Busca un usuario por su ID.
     * @param id ID del usuario.
     * @return Usuario.
     */
    Optional<Usuario> findById(Long id);

    /**
     * Realiza la validación de credenciales para el inicio de sesión.
     * @param email Email del usuario.
     * @param password Contraseña sin cifrar.
     * @return El objeto Usuario si la autenticación es exitosa.
     */
    Usuario autenticar(String email, String password);
}
