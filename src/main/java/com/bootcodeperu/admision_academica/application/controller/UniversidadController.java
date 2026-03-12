package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.universidad.UniversidadRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.universidad.UniversidadResponse;
import com.bootcodeperu.admision_academica.application.service.UniversidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/universidad")
@RequiredArgsConstructor
public class UniversidadController {
    private final UniversidadService universidadService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UniversidadResponse>>> listarTodas() {
        List<UniversidadResponse> universidades = universidadService.listarTodas();
        return ResponseEntity.ok(ApiResponse.ok(universidades, "Universidades obtenidas correctamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UniversidadResponse>> obtenerPorId(@PathVariable Long id) {
        UniversidadResponse universidad = universidadService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.ok(universidad, "Universidad obtenida correctamente"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UniversidadResponse>> crear(
            @Valid @RequestBody UniversidadRequest request) {
        UniversidadResponse creada = universidadService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(creada, "Universidad creada exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UniversidadResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UniversidadRequest request) {
        UniversidadResponse actualizada = universidadService.actualizar(id, request);
        return ResponseEntity.ok(ApiResponse.ok(actualizada, "Universidad actualizada exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        universidadService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Universidad eliminada exitosamente"));
    }
}
