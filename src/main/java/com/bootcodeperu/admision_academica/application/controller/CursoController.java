package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso.CursoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.curso.CursoResponse;
import com.bootcodeperu.admision_academica.application.service.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cursos")
@RequiredArgsConstructor
public class CursoController {
    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CursoResponse>>> getAllCursos() {
        return ResponseEntity.ok(
                ApiResponse.ok(cursoService.getAllCursos(), "Cursos obtenidos con éxito")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CursoResponse>> getCursoById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(cursoService.findCursoById(id), "Curso encontrado")
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CursoResponse>> createCurso(@RequestBody @Valid CursoRequest request) {
        return ResponseEntity.ok(
                ApiResponse.created(cursoService.createCurso(request), "Curso creado exitosamente")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CursoResponse>> updateCurso(
            @PathVariable Long id,
            @RequestBody @Valid CursoRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(cursoService.updateCurso(id, request), "Curso actualizado exitosamente")
        );
    }
}
