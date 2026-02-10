package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.flashcard.FlashcardRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.pregunta.PreguntaRequest;
import com.bootcodeperu.admision_academica.application.service.FlashcardService;
import com.bootcodeperu.admision_academica.application.service.PreguntaAdminService;
import com.bootcodeperu.admision_academica.application.service.TeoriaAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/contenido")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('CONTENIDO_CREATE')") // Solo usuarios con permiso CONTENIDO_CREATE (Admin)
public class ContenidoAdminController {

    private final PreguntaAdminService preguntaAdminService;
    private final FlashcardService flashcardService;
    private final TeoriaAdminService teoriaAdminService;

    // POST: Crear Pregunta (Práctica o Examen) -> Va a Outbox
    @PostMapping("/preguntas")
    public ResponseEntity<ApiResponse<Void>> createQuestion(@RequestBody @Valid PreguntaRequest request) {
        preguntaAdminService.createPregunta(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.empty("Pregunta en cola de procesamiento", 202));
    }

    // POST: Crear Flashcard -> Va a Outbox
    @PostMapping("/flashcards")
    public ResponseEntity<ApiResponse<Void>> createFlashcard(@RequestBody @Valid FlashcardRequest request) {
        flashcardService.createFlashcard(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.empty("Flashcard en cola de procesamiento", 202));
    }

    // POST: Crear Teoría (Sincrónico o Asincrónico según prefieras)
    @PostMapping("/teoria")
    public ResponseEntity<ApiResponse<ContenidoTeoriaResponse>> createTheory(@RequestBody @Valid ContenidoTeoriaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(teoriaAdminService.save(request), "Teoría creada exitosamente"));
    }

    @PutMapping("/teoria/{id}")
    public ResponseEntity<ApiResponse<ContenidoTeoriaResponse>> updateTheory(
            @PathVariable String id,
            @RequestBody @Valid ContenidoTeoriaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(teoriaAdminService.update(id, request), "Teoría actualizada"));
    }
}