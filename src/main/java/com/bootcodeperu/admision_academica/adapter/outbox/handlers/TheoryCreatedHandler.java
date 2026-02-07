package com.bootcodeperu.admision_academica.adapter.outbox.handlers;

import com.bootcodeperu.admision_academica.adapter.mapper.ContenidoTeoriaMapper;
import com.bootcodeperu.admision_academica.application.service.outbox.OutboxHandler;
import com.bootcodeperu.admision_academica.domain.model.Outbox;
import com.bootcodeperu.admision_academica.domain.model.OutboxEventType;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.ContenidoTeoria;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TheoryCreatedHandler implements OutboxHandler {

    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;
    private final ContenidoTeoriaMapper contenidoTeoriaMapper;

    @Override
    public boolean supports(OutboxEventType type) {
        return type == OutboxEventType.THEORY_CREATED;
    }

    @Override
    public void handle(Outbox event) throws Exception {
        // 1. Extraer datos del evento (Payload -> DTO)
        JsonNode root = objectMapper.readTree(event.getPayload());
        ContenidoTeoriaRequest dto = objectMapper.treeToValue(root.get("data"), ContenidoTeoriaRequest.class);

        // 2. Mapear DTO a Documento de MongoDB usando el Mapper
        // Esto soluciona automáticamente el error de List<SeccionContenido>
        ContenidoTeoria teoria = contenidoTeoriaMapper.toDocument(dto);

        // Seteamos metadatos de auditoría si no lo hace el mapper
        teoria.setFechaCreacion(LocalDateTime.now());
        teoria.setUltimaActualizacion(LocalDateTime.now());

        // 3. Guardar en MongoDB
        mongoTemplate.save(teoria);
    }
}