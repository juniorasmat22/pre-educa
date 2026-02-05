package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoResponse;
import com.bootcodeperu.admision_academica.application.service.PermisoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<ApiResponse<PermisoResponse>> createPermiso(@RequestBody @Valid PermisoRequest request) {
        return ResponseEntity.ok(
                ApiResponse.created(permisoService.createPermiso(request), "Permiso creado exitosamente")
        );
    }
}
