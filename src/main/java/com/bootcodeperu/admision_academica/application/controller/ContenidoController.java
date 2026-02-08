package com.bootcodeperu.admision_academica.application.controller;

import java.util.List;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaPracticaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.flashcard.FlashcardResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.progresotema.ProgresoTemaResponse;
import com.bootcodeperu.admision_academica.application.service.FlashcardService;
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
    private final FlashcardService flashcardService;

    // GET: Teoría por Tema
    @GetMapping("/temas/{temaId}/teoria")
    public ResponseEntity<ApiResponse<List<ContenidoTeoriaResponse>>> getTeoria(@PathVariable Long temaId) {
        return ResponseEntity.ok(
                ApiResponse.ok(contenidoService.getContenidoTeoriaByTemaId(temaId), "Teoría obtenida")
        );
    }

    // GET: Preguntas de Práctica (Filtradas por target PRACTICE)
    @GetMapping("/temas/{temaId}/practica")
    public ResponseEntity<ApiResponse<List<PreguntaPracticaResponse>>> getPreguntasPractica(
            @PathVariable Long temaId,
            @RequestParam String nivel) {
        return ResponseEntity.ok(
                ApiResponse.ok(contenidoService.getPreguntasPractica(temaId, nivel), "Preguntas de práctica obtenidas")
        );
    }

    // GET: Flashcards por Tema
    @GetMapping("/temas/{temaId}/flashcards")
    public ResponseEntity<ApiResponse<List<FlashcardResponse>>> getFlashcards(@PathVariable Long temaId) {
        return ResponseEntity.ok(
                ApiResponse.ok(flashcardService.getFlashcardsByTheme(temaId), "Flashcards obtenidas")
        );
    }

    // POST: Marcar teoría como completada
    @PostMapping("/temas/{temaId}/completar-teoria")
    public ResponseEntity<ApiResponse<ProgresoTemaResponse>> completarTeoria(
            @PathVariable Long temaId,
            @RequestParam Long usuarioId) {
        return ResponseEntity.ok(
                ApiResponse.ok(contenidoService.completarTeoria(usuarioId, temaId), "Progreso actualizado")
        );
    }
}
