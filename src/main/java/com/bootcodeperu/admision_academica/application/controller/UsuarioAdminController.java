package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import com.bootcodeperu.admision_academica.application.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // todo este controlador es solo para admins
public class UsuarioAdminController {
    private final UsuarioService usuarioService;

    // Asignar rol a un usuario
    @PatchMapping("/{userId}/rol/{rolId}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> assignRol(
            @PathVariable Long userId,
            @PathVariable Long rolId
    ) {
        UsuarioResponse usuarioActualizado =
                usuarioService.assignRoleToUser(userId, rolId);

        return ResponseEntity.ok(
                ApiResponse.ok(usuarioActualizado, "Rol asignado correctamente")
        );
    }

    // Bloquear un usuario
    @PatchMapping("/{userId}/bloquear")
    public ResponseEntity<ApiResponse<UsuarioResponse>> bloquearUsuario(@PathVariable Long userId) {
        UsuarioResponse usuarioActualizado = usuarioService.blockUser(userId);
        return ResponseEntity.ok(ApiResponse.ok(usuarioActualizado, "Usuario bloqueado"));
    }

    // Desbloquear un usuario
    @PatchMapping("/{userId}/desbloquear")
    public ResponseEntity<ApiResponse<UsuarioResponse>> desbloquearUsuario(@PathVariable Long userId) {
        UsuarioResponse usuarioActualizado = usuarioService.unblockUser(userId);
        return ResponseEntity.ok(ApiResponse.ok(usuarioActualizado, "Usuario desbloqueado"));
    }

    // Activar un usuario
    @PatchMapping("/{userId}/activar")
    public ResponseEntity<ApiResponse<UsuarioResponse>> activarUsuario(@PathVariable Long userId) {
        UsuarioResponse usuarioActualizado = usuarioService.activateUser(userId);
        return ResponseEntity.ok(ApiResponse.ok(usuarioActualizado, "Usuario activado"));
    }
    // Desactivar un usuario
    @PatchMapping("/{userId}/desactivar")
    public ResponseEntity<ApiResponse<UsuarioResponse>> desactivarUsuario(@PathVariable Long userId) {
        UsuarioResponse usuarioActualizado = usuarioService.deactivateUser(userId);
        return ResponseEntity.ok(ApiResponse.ok(usuarioActualizado, "Usuario desactivado"));
    }

    // Cambio de contraseña
    @PatchMapping("/{userId}/cambiar-password")
    public ResponseEntity<ApiResponse<UsuarioResponse>> cambiarPassword(
            @PathVariable Long userId,
            @RequestParam String nuevaPassword
    ) {
        UsuarioResponse usuarioActualizado = usuarioService.changePassword(userId, nuevaPassword);
        return ResponseEntity.ok(ApiResponse.ok(usuarioActualizado, "Contraseña actualizada correctamente"));
    }
    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioResponse>>> getAllUsuarios() {
        List<UsuarioResponse> usuarios = usuarioService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.ok(usuarios, "Lista de usuarios"));
    }
}

