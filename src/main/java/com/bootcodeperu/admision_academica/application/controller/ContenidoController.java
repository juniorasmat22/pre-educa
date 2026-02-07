package com.bootcodeperu.admision_academica.application.controller;

import java.util.List;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaPracticaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.ContenidoTeoria;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaResponse;
import com.bootcodeperu.admision_academica.application.service.ContenidoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/contenido")
@RequiredArgsConstructor
public class ContenidoController {
    
    private final ContenidoService contenidoService;

    // GET /api/v1/contenido/temas/1/teoria
    @GetMapping("/temas/{temaId}/teoria")
    @PreAuthorize("hasAuthority('CONTENIDO_READ') or hasRole('ADMIN')") // Permiso necesario
    public ResponseEntity<List<ContenidoTeoriaResponse>> getTeoriaByTema(@PathVariable Long temaId) {
        List<ContenidoTeoriaResponse> teoria = contenidoService.getContenidoTeoriaByTemaId(temaId);
        return ResponseEntity.ok(teoria);
    }
    @GetMapping("/temas/{temaId}/preguntas")
    @PreAuthorize("hasAuthority('CONTENIDO_READ') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PreguntaPracticaResponse>>> getPreguntasByTema(
            @PathVariable Long temaId,
            @RequestParam String nivel) {
        List<PreguntaPracticaResponse> preguntas = contenidoService.getPreguntasPractica(temaId, nivel);
        return ResponseEntity.ok(ApiResponse.ok(preguntas, "Preguntas de práctica obtenidas correctamente"));
    }
}
