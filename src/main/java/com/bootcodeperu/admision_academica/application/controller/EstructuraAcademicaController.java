package com.bootcodeperu.admision_academica.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/areas")
    public ResponseEntity<List<Area>> getAllAreas() {
        return ResponseEntity.ok(estructuraService.getAllAreas());
    }
    
    // 2. Obtener todos los cursos
    @GetMapping("/cursos")
    public ResponseEntity<List<Curso>> getAllCursos() {
        return ResponseEntity.ok(estructuraService.getAllCursos());
    }
    
    // 3. Obtener los temas de un curso
    // GET /api/v1/estructura/cursos/1/temas
    @GetMapping("/cursos/{cursoId}/temas")
    public ResponseEntity<List<TemaResponse>> getTemasByCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(contenidoService.getTemasByCursoId(cursoId));
    }
    
    // 4. Obtener la distribución de preguntas por área
    // GET /api/v1/estructura/distribucion?areaId=1
    @GetMapping("/distribucion")
    public ResponseEntity<List<CursoArea>> getDistribucion(@RequestParam Long areaId) {
        return ResponseEntity.ok(estructuraService.getDistribucionByArea(areaId));
    }
    
    // [ADMIN] POST /api/v1/estructura/areas (Se protegería con Spring Security)
    // @PostMapping("/areas")
    // public ResponseEntity<Area> createArea(@RequestBody Area area) {
    //     return ResponseEntity.ok(estructuraService.saveArea(area));
    // }
}