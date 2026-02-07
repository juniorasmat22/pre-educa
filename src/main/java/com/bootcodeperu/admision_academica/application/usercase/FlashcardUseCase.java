package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.application.controller.dto.flashcard.FlashcardRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.flashcard.FlashcardResponse;
import com.bootcodeperu.admision_academica.application.service.FlashcardService;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.MetadatoPregunta;
import com.bootcodeperu.admision_academica.domain.model.Outbox;
import com.bootcodeperu.admision_academica.domain.model.QuestionTarget;
import com.bootcodeperu.admision_academica.domain.repository.MetadatoPreguntaRepository;
import com.bootcodeperu.admision_academica.domain.repository.OutboxRepository;
import com.bootcodeperu.admision_academica.domain.repository.TemaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FlashcardUseCase implements FlashcardService {
    private final TemaRepository temaRepository;
    private final MetadatoPreguntaRepository metadatoRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void createFlashcard(FlashcardRequest request) {
        // 1. Validar en Postgres
        var tema = temaRepository.findById(request.idTema())
                .orElseThrow(() -> new ResourceNotFoundException("Tema no encontrado"));

        // 2. Metadato en Postgres (Target: FLASHCARD)
        MetadatoPregunta metadato = new MetadatoPregunta();
        metadato.setTema(tema);
        metadato.setTarget(QuestionTarget.FLASHCARD);
        var savedMetadato = metadatoRepository.save(metadato);

        // 3. Al Outbox con tipo específico
        saveToOutbox(savedMetadato.getId(), request, "FLASHCARD_CREATED_EVENT");
    }

    private void saveToOutbox(Long metadatoId, Object data, String type) {
        try {
            Map<String, Object> payload = Map.of("metadatoId", metadatoId, "data", data);
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
    public List<FlashcardResponse> getFlashcardsByTheme(Long themeId) {
        return List.of();
    }
}
