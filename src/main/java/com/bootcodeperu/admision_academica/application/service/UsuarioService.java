package com.bootcodeperu.admision_academica.application.service;

import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioRegistroRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import com.bootcodeperu.admision_academica.domain.model.Usuario;

public interface UsuarioService {
	/**
     * Registra un nuevo usuario, incluyendo el hash de la contraseña.
     * @param usuario El objeto Usuario a registrar.
     * @return El Usuario creado.
     */
    UsuarioResponse registerUser(UsuarioRegistroRequest usuario);

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
    Usuario authenticate(String email, String password);

    UsuarioResponse getUserById(Long id);
    UsuarioResponse getUserByEmail(String email);
    List<UsuarioResponse> getAllUsers();
    UsuarioResponse assignRoleToUser(Long userId, Long rolId);

    /**
     * Bloquea a un usuario impidiendo su autenticación.
     */
    UsuarioResponse blockUser(Long userId);

    /**
     * Desbloquea un usuario previamente bloqueado.
     */
    UsuarioResponse unblockUser(Long userId);

    /**
     * Cambia el estado activo/inactivo de un usuario.
     */
    UsuarioResponse activateUser(Long userId);
    UsuarioResponse deactivateUser(Long userId);

    /**
     * Cambia la contraseña de un usuario (requiere verificación de permisos o propia contraseña)
     */
    UsuarioResponse changePassword(Long userId, String newPassword);

}
