package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolResponseMini;
import com.bootcodeperu.admision_academica.application.service.PermisoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/permisos")
@RequiredArgsConstructor
public class PermisoController {
    private final PermisoService permisoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermisoResponse>>> getAllPermisos() {
        return ResponseEntity.ok(
                ApiResponse.ok(permisoService.getAllPermisos(), "Lista de permisos obtenida")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PermisoResponse>> getPermisoById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(permisoService.getPermisoById(id), "Permiso encontrado"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PermisoResponse>> createPermiso(@RequestBody @Valid PermisoRequest request) {
        return ResponseEntity.ok(
                ApiResponse.created(permisoService.createPermiso(request), "Permiso creado exitosamente")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PermisoResponse>> updatePermiso(
            @PathVariable Long id,
            @RequestBody @Valid PermisoRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(permisoService.updatePermiso(id, request), "Permiso actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePermiso(@PathVariable Long id) {
        permisoService.deletePermiso(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Permiso eliminado"));
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<ApiResponse<Set<RolResponseMini>>> getRolesByPermiso(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(permisoService.getRolesByPermiso(id), "Roles asociados al permiso")
        );
    }
}
