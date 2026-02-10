package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.mapper.FlashcardMapper;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.Flashcard;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.PreguntaDetalle;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.repository.FlashcardMongoRepository;
import com.bootcodeperu.admision_academica.application.controller.dto.flashcard.FlashcardRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.flashcard.FlashcardResponse;
import com.bootcodeperu.admision_academica.application.service.FlashcardService;
import com.bootcodeperu.admision_academica.domain.exception.ContentLoadingException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.MetadatoPregunta;
import com.bootcodeperu.admision_academica.domain.model.Outbox;
import com.bootcodeperu.admision_academica.domain.model.enums.QuestionTarget;
import com.bootcodeperu.admision_academica.domain.repository.MetadatoPreguntaRepository;
import com.bootcodeperu.admision_academica.domain.repository.OutboxRepository;
import com.bootcodeperu.admision_academica.domain.repository.TemaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlashcardUseCase implements FlashcardService {
    private final TemaRepository temaRepository;
    private final MetadatoPreguntaRepository metadatoRepository;
    private final FlashcardMongoRepository flashcardMongoRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final FlashcardMapper flashcardMapper;

    @Override
    @Transactional
    public void createFlashcard(FlashcardRequest request) {
        // 1. Validar en Postgres
        var tema = temaRepository.findById(request.idTema())
                .orElseThrow(() -> new ResourceNotFoundException("Tema no encontrado"));

        // 2. Metadato en Postgres (Target: FLASHCARD)
        MetadatoPregunta metadato = new MetadatoPregunta();
        metadato.setTema(tema);
        metadato.setNivel(request.nivel());
        metadato.setTarget(QuestionTarget.FLASHCARD);
        var savedMetadato = metadatoRepository.save(metadato);

        // 3. Al Outbox con tipo específico
        saveToOutbox(savedMetadato.getId(), request, "FLASHCARD_CREATED_EVENT");
    }

    private void saveToOutbox(Long metadatoId, Object data, String type) {
        try {
            Map<String, Object> payload = Map.of("sqlMetadatoId", metadatoId, "data", data);
            Outbox event = new Outbox();
            event.setTipoEvento(type);
            event.setPayload(objectMapper.writeValueAsString(payload));
            event.setEstado("PENDIENTE");
            outboxRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing Flashcard");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlashcardResponse> getFlashcardsByTheme(Long themeId) {
        // 1. Obtener Metadatos desde PostgreSQL
        // Filtramos específicamente por el TEMA y el tipo FLASHCARD
        List<MetadatoPregunta> metadatos = metadatoRepository.findByTemaIdAndTarget(themeId, QuestionTarget.FLASHCARD);

        if (metadatos.isEmpty()) {
            return List.of(); // Es mejor devolver lista vacía que lanzar excepción si el tema no tiene cards aún
        }

        // 2. Extraer IDs de MongoDB y filtrar posibles nulos (si el Outbox no ha procesado)
        List<String> mongoIds = metadatos.stream()
                .map(MetadatoPregunta::getMongoIdPregunta)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (mongoIds.isEmpty()) {
            return List.of();
        }

        // 3. Obtener los documentos desde MongoDB
        List<Flashcard> flashcards = flashcardMongoRepository.findByIdTemaSQL(themeId);

        // 4. Validación de Integridad (Opcional pero recomendado)
        if (flashcards.size() != mongoIds.size()) {
            // Log de advertencia: Indica que hay metadatos en SQL cuyos documentos en Mongo no existen
            System.err.println("Advertencia: Inconsistencia detectada. Metadatos: " + mongoIds.size() + ", Documentos: " + flashcards.size());
        }

        // 5. Mapear a DTOs de respuesta
        return flashcards.stream()
                .map(flashcardMapper::toResponse)
                .collect(Collectors.toList());
    }
}
