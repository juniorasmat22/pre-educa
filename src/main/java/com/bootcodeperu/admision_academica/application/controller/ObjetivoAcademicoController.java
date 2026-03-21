package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.objetivo.ObjetivoAcademicoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.objetivo.ObjetivoAcademicoResponse;
import com.bootcodeperu.admision_academica.application.service.ObjetivoAcademicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/objetivos-academicos")
@RequiredArgsConstructor
public class ObjetivoAcademicoController {

    private final ObjetivoAcademicoService objetivoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ObjetivoAcademicoResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(objetivoService.listarTodos(), "Objetivos obtenidos"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ObjetivoAcademicoResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(objetivoService.obtenerPorId(id), "Objetivo obtenido"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ObjetivoAcademicoResponse>> actualizar(@PathVariable Long id, @Valid @RequestBody ObjetivoAcademicoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(objetivoService.actualizar(id, request), "Objetivo actualizado"));
    }
}