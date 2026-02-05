package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.tema.TemaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.tema.TemaResponseDetalle;
import com.bootcodeperu.admision_academica.application.service.TemaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/temas")
@RequiredArgsConstructor
public class TemaController {

    private final TemaService temaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TemaResponseDetalle>>> getAllTemas() {
        return ResponseEntity.ok(
                ApiResponse.ok(temaService.getAllTemas(), "Lista de temas obtenidos correctamente")
        );
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<ApiResponse<List<TemaResponseDetalle>>> getTemasByCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(
                ApiResponse.ok(temaService.getAllTemasByCursoId(cursoId), "Los temas del curso se obtuvieron correctamente")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TemaResponseDetalle>> getTemaById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(temaService.findTemaById(id), "Tema encontrado")
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TemaResponseDetalle>> createTema(@RequestBody @Valid TemaRequest request) {
        return ResponseEntity.ok(
                ApiResponse.created(temaService.createTema(request), "Tema creado exitosamente")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TemaResponseDetalle>> updateTema(
            @PathVariable Long id,
            @RequestBody @Valid TemaRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(temaService.updateTema(id, request), "Tema actualizado exitosamente")
        );
    }

}