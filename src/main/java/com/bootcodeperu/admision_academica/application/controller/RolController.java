package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolPermisosRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolUpdateRequest;
import com.bootcodeperu.admision_academica.application.service.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RolController {
    private final RolService rolService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RolResponse>>> getAllRoles() {
        return ResponseEntity.ok(
                ApiResponse.ok(rolService.getAllRoles(), "Lista de roles obtenida")
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RolResponse>> getRolById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        rolService.getRolById(id),
                        "Rol obtenido correctamente"
                )
        );
    }
    @PostMapping
    public ResponseEntity<ApiResponse<RolResponse>> createRol(@RequestBody @Valid RolRequest request) {
        return ResponseEntity.ok(
                ApiResponse.created(rolService.createRol(request), "Rol creado exitosamente")
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RolResponse>> updateRol(
            @PathVariable Long id,
            @RequestBody @Valid RolUpdateRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        rolService.updateRol(id, request),
                        "Rol actualizado correctamente"
                )
        );
    }
    @PatchMapping("/{id}/permisos")
    public ResponseEntity<ApiResponse<RolResponse>> addPermisos(
            @PathVariable Long id,
            @RequestBody @Valid RolPermisosRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        rolService.addPermisos(id, request),
                        "Permisos asignados correctamente"
                )
        );
    }
    @PatchMapping("/{id}/permisos/remove")
    public ResponseEntity<ApiResponse<RolResponse>> removePermisos(
            @PathVariable Long id,
            @RequestBody @Valid RolPermisosRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        rolService.removePermisos(id, request),
                        "Permisos eliminados correctamente"
                )
        );
    }
    @GetMapping("/{id}/permisos")
    public ResponseEntity<ApiResponse<Set<PermisoResponse>>> getPermisosByRol(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        rolService.getPermisosByRol(id),
                        "Lista de permisos del rol obtenida correctamente"
                )
        );
    }

}
