package com.bootcodeperu.admision_academica.application.usercase;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.PreguntaDetalle;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.repository.PreguntaDetalleMongoRepository;
import com.bootcodeperu.admision_academica.application.controller.dto.PreguntaCreationDTO;
import com.bootcodeperu.admision_academica.application.controller.dto.outbox.OutboxPreguntaPayload;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.MetadatoPregunta;
import com.bootcodeperu.admision_academica.domain.model.Outbox;
import com.bootcodeperu.admision_academica.domain.repository.MetadatoPreguntaRepository;
import com.bootcodeperu.admision_academica.domain.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutboxProcessor {

    private final OutboxRepository outboxRepository;
    private final PreguntaDetalleMongoRepository preguntaDetalleMongoRepository;
    private final MetadatoPreguntaRepository metadatoPreguntaRepository; // Para actualizar el ID de Mongo en SQL
    private final ObjectMapper objectMapper;
 // 1. Instancia estática y final del Logger
    private static final Logger log = LoggerFactory.getLogger(OutboxProcessor.class);
    // Se ejecuta cada 10 segundos
    @Scheduled(fixedRate = 10000) 
    @Transactional // Cada procesamiento es una transacción SQL
    public void processOutboxEvents() {
    	log.info("Iniciando procesamiento de eventos Outbox pendientes."); // Nivel INFO para el inicio del ciclo
    	
        // 1. Leer eventos pendientes (máx 50)
        List<Outbox> eventos = outboxRepository.findTopPendingEvents();
        if (eventos.isEmpty()) {
            log.debug("No se encontraron eventos pendientes para procesar.");
            return;
        }
        log.debug("Se encontraron {} eventos pendientes.", eventos.size()); // Nivel DEBUG para detalles internos
        for (Outbox evento : eventos) {
            try {
                if ("PREGUNTA_CREADA_EVENT".equals(evento.getTipoEvento())) {
                    
                    // 2. Deserializar el Payload (JSON a DTO)
                    //PreguntaCreationDTO dto = objectMapper.readValue(evento.getPayload(), PreguntaCreationDTO.class);
                    OutboxPreguntaPayload payload = objectMapper.readValue(evento.getPayload(), OutboxPreguntaPayload.class);
                    PreguntaCreationDTO dto = payload.dto(); // El DTO con el contenido de la pregunta
                    Long sqlMetadatoId = payload.sqlMetadatoId(); // El ID de PostgreSQL a actualizar
                    
                    // 3. Escribir en MongoDB (Operación Externa)
                    PreguntaDetalle detalle = new PreguntaDetalle();
                    detalle.setIdTemaSQL(dto.getIdTema()); 
                    detalle.setEnunciado(dto.getEnunciado());
                    detalle.setOpciones(dto.getOpciones());
                    detalle.setRespuestaCorrecta(dto.getRespuestaCorrecta());
                    
                    // Guardar en Mongo. Esto puede fallar si la conexión a Mongo Atlas está caída.
                    PreguntaDetalle mongoDetalle = preguntaDetalleMongoRepository.save(detalle);
                    // --- 4. Sincronización con PostgreSQL (Commit del ID de Mongo) ---
                    
                    // 4a. Buscar el Metadato en SQL
                    MetadatoPregunta metadato = metadatoPreguntaRepository.findById(sqlMetadatoId)
                        .orElseThrow(() -> new ResourceNotFoundException("Metadato", "id", sqlMetadatoId));

                    // 4b. Actualizar el campo clave
                    metadato.setMongoIdPregunta(mongoDetalle.getId());
                    metadatoPreguntaRepository.save(metadato);

                    // 5. Marcar como completado
                    evento.setEstado("COMPLETADO");
                    outboxRepository.save(evento);
                    log.info("Evento Outbox {} procesado y completado exitosamente.", evento.getId());
                }
            } catch (Exception e) {
                // Si Mongo falla aquí, el evento se marca como ERROR, la transacción SQL se revierte
                // y se reintentará en el próximo ciclo del scheduler.
                evento.setEstado("ERROR");
                outboxRepository.save(evento);
                log.error("CRÍTICO: Fallo al procesar evento Outbox ID {}. Se reintentará. Causa: {}", 
                        evento.getId(), e.getMessage(), e);
            }
        }
    }
}