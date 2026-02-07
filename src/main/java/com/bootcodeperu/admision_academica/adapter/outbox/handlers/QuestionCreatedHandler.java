package com.bootcodeperu.admision_academica.adapter.outbox.handlers;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.PreguntaDetalle;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.repository.PreguntaDetalleMongoRepository;
import com.bootcodeperu.admision_academica.application.controller.dto.PreguntaCreationDTO;
import com.bootcodeperu.admision_academica.application.controller.dto.outbox.OutboxPreguntaPayload;
import com.bootcodeperu.admision_academica.application.service.outbox.OutboxHandler;
import com.bootcodeperu.admision_academica.domain.model.Outbox;
import com.bootcodeperu.admision_academica.domain.model.OutboxEventType;
import com.bootcodeperu.admision_academica.domain.repository.MetadatoPreguntaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionCreatedHandler implements OutboxHandler {
    private final PreguntaDetalleMongoRepository mongoRepository;
    private final MetadatoPreguntaRepository sqlRepository;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(OutboxEventType type) {
        return type == OutboxEventType.QUESTION_CREATED;
    }

    @Override
    @Transactional
    public void handle(Outbox event) throws Exception {
        OutboxPreguntaPayload payload = objectMapper.readValue(event.getPayload(), OutboxPreguntaPayload.class);
        PreguntaCreationDTO dto = payload.dto();

        // 1. Mapeo y persistencia en Mongo
        PreguntaDetalle detalle = new PreguntaDetalle();
        detalle.setIdTemaSQL(dto.getIdTema());
        detalle.setEnunciado(dto.getEnunciado());
        detalle.setOpciones(dto.getOpciones());
        detalle.setRespuestaCorrecta(dto.getRespuestaCorrecta());
        //detalle.setExplicacionCorrecta(dto.getExplicacionCorrecta());
        //detalle.setExplicacionIncorrecta(dto.getExplicacionIncorrecta());

        PreguntaDetalle savedMongo = mongoRepository.save(detalle);

        // 2. Sincronización con SQL
        sqlRepository.findById(payload.sqlMetadatoId())
                .ifPresent(meta -> {
                    meta.setMongoIdPregunta(savedMongo.getId());
                    sqlRepository.save(meta);
                });
    }
}
