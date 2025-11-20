package com.bootcodeperu.admision_academica.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcodeperu.admision_academica.application.service.SeguridadService;
import com.bootcodeperu.admision_academica.domain.model.Permiso;
import com.bootcodeperu.admision_academica.domain.model.Rol;
import com.bootcodeperu.admision_academica.domain.model.Usuario;

import lombok.RequiredArgsConstructor;

//DTO simple para la asignación de roles
record AsignacionRolRequest(String rolName) {}

@RestController
@RequestMapping("/api/v1/admin/seguridad")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Proteger todo el controlador para solo administradores
public class SeguridadController {

 private final SeguridadService seguridadService;

 // --- ROL MANAGEMENT ---

 @PostMapping("/roles")
 public ResponseEntity<Rol> createRol(@RequestBody Rol rol) {
     return ResponseEntity.ok(seguridadService.saveRol(rol));
 }
 
 @GetMapping("/roles")
 public ResponseEntity<List<Rol>> getAllRoles() {
     return ResponseEntity.ok(seguridadService.findAllRoles());
 }

 // --- PERMISSION MANAGEMENT ---
 
 @PostMapping("/permisos")
 public ResponseEntity<Permiso> createPermiso(@RequestBody Permiso permiso) {
     return ResponseEntity.ok(seguridadService.savePermiso(permiso));
 }

 // --- USER ASSIGNMENT ---

 // POST /api/v1/admin/seguridad/usuarios/5/asignar-rol
 @PutMapping("/usuarios/{userId}/asignar-rol")
 public ResponseEntity<Usuario> asignarRolAUsuario(
         @PathVariable Long userId,
         @RequestBody AsignacionRolRequest request) {
     
     Usuario usuarioActualizado = seguridadService.asignarRolAUsuario(userId, request.rolName());
     return ResponseEntity.ok(usuarioActualizado);
 }
}
