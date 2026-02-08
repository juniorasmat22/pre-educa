package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.application.controller.dto.pregunta.PreguntaRequest;
import com.bootcodeperu.admision_academica.application.service.PreguntaAdminService;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.MetadatoPregunta;
import com.bootcodeperu.admision_academica.domain.model.Outbox;
import com.bootcodeperu.admision_academica.domain.model.Tema;
import com.bootcodeperu.admision_academica.domain.model.enums.OutboxEventType;
import com.bootcodeperu.admision_academica.domain.repository.MetadatoPreguntaRepository;
import com.bootcodeperu.admision_academica.domain.repository.OutboxRepository;
import com.bootcodeperu.admision_academica.domain.repository.TemaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PreguntasAdminUseCase implements PreguntaAdminService {
    private final TemaRepository temaRepository;
    private final MetadatoPreguntaRepository metadatoRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void createPregunta(PreguntaRequest request) {
        Tema tema = temaRepository.findById(request.idTema())
                .orElseThrow(() -> new ResourceNotFoundException("Tema", "id", request.idTema()));

        MetadatoPregunta meta = new MetadatoPregunta();
        meta.setTema(tema);
        meta.setTarget(request.target());
        meta.setNivel(request.nivel());
        meta.setAnioExamen(request.anioExamen());
        meta.setTipoPregunta(request.tipoPregunta());
        var savedMeta = metadatoRepository.save(meta);

        // Registro de Outbox (Sincronización asíncrona)
        try {
            Map<String, Object> payload = Map.of("sqlMetadatoId", savedMeta.getId(), "data", request);
            Outbox event = new Outbox();
            event.setTipoEvento(OutboxEventType.QUESTION_CREATED.getValue());
            event.setPayload(objectMapper.writeValueAsString(payload));
            event.setEstado("PENDIENTE");
            outboxRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar Outbox");
        }
    }
}
