package com.bootcodeperu.admision_academica.application.controller;

import java.util.List;

import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso.CursoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.curso.CursoResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CursoAreaResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bootcodeperu.admision_academica.application.controller.dto.tema.TemaResponse;
import com.bootcodeperu.admision_academica.application.service.ContenidoService;
import com.bootcodeperu.admision_academica.application.service.EstructuraAcademicaService;
import com.bootcodeperu.admision_academica.domain.model.Area;
import com.bootcodeperu.admision_academica.domain.model.Curso;
import com.bootcodeperu.admision_academica.domain.model.CursoArea;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/estructura")
@RequiredArgsConstructor
public class EstructuraAcademicaController {

    private final EstructuraAcademicaService estructuraService;
    private final ContenidoService contenidoService;

    // 1. Obtener todas las áreas (A, B, C, D)
    // GET api/v1/estructura/areas
    @GetMapping("/areas")
    public ResponseEntity<List<AreaResponse>> getAllAreas() {
        return ResponseEntity.ok(estructuraService.getAllAreas());
    }
    //2. Crear áreas
    // POST api/v1/estructura/areas
    @PostMapping("/areas")
    public ResponseEntity<AreaResponse> createArea(@Valid @RequestBody AreaRequest area) {
        return ResponseEntity.ok(estructuraService.saveArea(area));
    }
    // 3. Obtener todos los cursos
    // GET api/v1/estructura/cursos
    @GetMapping("/cursos")
    public ResponseEntity<List<CursoResponse>> getAllCursos() {
        return ResponseEntity.ok(estructuraService.getAllCursos());
    }
    
    // 4. Obtener los temas de un curso
    // GET /api/v1/estructura/cursos/1/temas
    @GetMapping("/cursos/{cursoId}/temas")
    public ResponseEntity<List<TemaResponse>> getTemasByCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(contenidoService.getTemasByCursoId(cursoId));
    }
    
    // 5. Obtener la distribución de preguntas por área
    // GET /api/v1/estructura/distribucion?areaId=1
    @GetMapping("/distribucion")
    public ResponseEntity<List<CursoAreaResponse>> getDistribucion(@RequestParam Long areaId) {
        return ResponseEntity.ok(estructuraService.getDistribucionByArea(areaId));
    }
    //6. Crear curso
    // POST api/v1/estructura/cursos
    @PostMapping("/cursos")
    public ResponseEntity<CursoResponse> createCurso(@Valid @RequestBody CursoRequest request) {
        return ResponseEntity.ok(estructuraService.saveCurso(request));
    }
}