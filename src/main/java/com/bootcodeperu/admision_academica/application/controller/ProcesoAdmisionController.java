package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.procesoadmision.ProcesoAdmisionRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.procesoadmision.ProcesoAdmisionResponse;
import com.bootcodeperu.admision_academica.application.service.ProcesoAdmisionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/procesos-admision")
@RequiredArgsConstructor
public class ProcesoAdmisionController {

    private final ProcesoAdmisionService procesoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProcesoAdmisionResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(procesoService.listarTodos(), "Procesos obtenidos"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProcesoAdmisionResponse>> crear(@Valid @RequestBody ProcesoAdmisionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(procesoService.crear(request), "Proceso creado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProcesoAdmisionResponse>> actualizar(@PathVariable Long id, @Valid @RequestBody ProcesoAdmisionRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(procesoService.actualizar(id, request), "Proceso actualizado"));
    }
}
