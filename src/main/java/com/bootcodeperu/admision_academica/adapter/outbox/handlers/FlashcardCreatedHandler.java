package com.bootcodeperu.admision_academica.adapter.outbox.handlers;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.Flashcard;
import com.bootcodeperu.admision_academica.application.controller.dto.flashcard.FlashcardRequest;
import com.bootcodeperu.admision_academica.application.service.outbox.OutboxHandler;
import com.bootcodeperu.admision_academica.domain.model.Outbox;
import com.bootcodeperu.admision_academica.domain.model.OutboxEventType;
import com.bootcodeperu.admision_academica.domain.repository.MetadatoPreguntaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FlashcardCreatedHandler implements OutboxHandler {

    private final MongoTemplate mongoTemplate;
    private final MetadatoPreguntaRepository sqlRepository;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(OutboxEventType type) {
        return type == OutboxEventType.FLASHCARD_CREATED;
    }

    @Override
    @Transactional
    public void handle(Outbox event) throws Exception {
        // 1. Deserializar el payload (que contiene metadatoId y los datos de la flashcard)
        JsonNode root = objectMapper.readTree(event.getPayload());
        Long metadatoId = root.get("metadatoId").asLong();
        FlashcardRequest dto = objectMapper.treeToValue(root.get("data"), FlashcardRequest.class);

        // 2. Mapear y guardar en la colección 'flashcards' de MongoDB
        Flashcard flashcard = new Flashcard();
        flashcard.setIdTemaSQL(dto.idTema());
        flashcard.setFrente(dto.frente());
        flashcard.setDorso(dto.dorso());
        flashcard.setImagenUrl(dto.imagenUrl());

        Flashcard savedMongo = mongoTemplate.save(flashcard);

        // 3. Sincronizar con PostgreSQL: Actualizar el Metadato con el ID de Mongo
        sqlRepository.findById(metadatoId).ifPresent(meta -> {
            meta.setMongoIdPregunta(savedMongo.getId());
            sqlRepository.save(meta);
        });
    }
}