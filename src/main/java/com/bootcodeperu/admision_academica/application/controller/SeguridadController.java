package com.bootcodeperu.admision_academica.application.controller;

import java.util.List;

import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import com.bootcodeperu.admision_academica.application.service.SeguridadService;
import com.bootcodeperu.admision_academica.domain.model.Permiso;
import com.bootcodeperu.admision_academica.domain.model.Rol;

import lombok.RequiredArgsConstructor;

//DTO simple para la asignación de roles
record AsignacionRolRequest(String rolName) {}

@RestController
@RequestMapping("/api/v1/admin/seguridad")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Proteger todo el controlador para solo administradores
public class SeguridadController {

 private final SeguridadService seguridadService;


 // --- USER ASSIGNMENT ---

 // POST /api/v1/admin/seguridad/usuarios/5/asignar-rol
 @PutMapping("/usuarios/{userId}/asignar-rol")
 public ResponseEntity<UsuarioResponse> asignarRolAUsuario(
         @PathVariable Long userId,
         @RequestBody AsignacionRolRequest request) {
     
     UsuarioResponse usuarioActualizado = seguridadService.asignarRolAUsuario(userId, request.rolName());
     return ResponseEntity.ok(usuarioActualizado);
 }
}
