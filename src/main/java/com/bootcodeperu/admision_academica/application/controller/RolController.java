package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolResponse;
import com.bootcodeperu.admision_academica.application.service.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<ApiResponse<RolResponse>> createRol(@RequestBody @Valid RolRequest request) {
        return ResponseEntity.ok(
                ApiResponse.created(rolService.createRol(request), "Rol creado exitosamente")
        );
    }
}
