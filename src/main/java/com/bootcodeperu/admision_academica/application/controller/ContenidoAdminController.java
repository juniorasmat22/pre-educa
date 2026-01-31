package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.ContenidoTeoria;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.PreguntaDetalle;
import com.bootcodeperu.admision_academica.application.controller.dto.PreguntaCreationDTO;
import com.bootcodeperu.admision_academica.application.controller.dto.outbox.OutboxPreguntaPayload;
import com.bootcodeperu.admision_academica.application.service.ContenidoService;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.MetadatoPregunta;
import com.bootcodeperu.admision_academica.domain.model.Outbox;
import com.bootcodeperu.admision_academica.domain.model.Tema;
import com.bootcodeperu.admision_academica.domain.repository.MetadatoPreguntaRepository;
import com.bootcodeperu.admision_academica.domain.repository.OutboxRepository;
import com.bootcodeperu.admision_academica.domain.repository.TemaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/contenido")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('CONTENIDO_CREATE')") // Solo usuarios con permiso CONTENIDO_CREATE (Admin)
public class ContenidoAdminController {

    private final TemaRepository temaRepository;
    private final MetadatoPreguntaRepository metadatoPreguntaRepository;
    private final ContenidoService contenidoService;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    /**
     * Inserta una nueva pregunta en ambas BD (MongoDB y PostgreSQL).
     */
    @PostMapping("/preguntas")
    @Transactional // Asegura que la operación sea atómica en PostgreSQL
    public ResponseEntity<MetadatoPregunta> crearNuevaPregunta(@RequestBody PreguntaCreationDTO dto) {

        // 1. Verificar si el tema existe (PostgreSQL)
        Tema tema = temaRepository.findById(dto.getIdTema())
                .orElseThrow(() -> new ResourceNotFoundException("Tema", "id", dto.getIdTema()));
    	
        // 2. Insertar el detalle de la pregunta en MongoDB
        PreguntaDetalle detalle = new PreguntaDetalle();
        // Nota: Asumiendo que has agregado setters para estos campos en PreguntaDetalle.java
        detalle.setIdTemaSQL(dto.getIdTema());
        detalle.setEnunciado(dto.getEnunciado());
        detalle.setOpciones(dto.getOpciones());
        detalle.setRespuestaCorrecta(dto.getRespuestaCorrecta());
        
        // Guardar en Mongo. Mongo asigna el _id (String) automáticamente.
        //PreguntaDetalle mongoDetalle = preguntaDetalleMongoRepository.save(detalle);

        // 3. Insertar el Metadato en PostgreSQL usando el ID de Mongo
        MetadatoPregunta metadato = new MetadatoPregunta();
        metadato.setTema(tema);
        //metadato.setMongoIdPregunta(mongoDetalle.getId()); // Usamos el _id generado por Mongo
        metadato.setNivel(dto.getNivel());
        metadato.setTipoPregunta(dto.getTipoPregunta());
        metadato.setAnioExamen(dto.getAnioExamen());

        // Guardar en PostgreSQL
        MetadatoPregunta sqlMetadato = metadatoPreguntaRepository.save(metadato);

        String payloadJson;
        try {
            // Incluimos el ID de la Entidad SQL para que el consumidor sepa qué actualizar
            // Clonamos el DTO para incluir el ID de PostgreSQL generado
        	OutboxPreguntaPayload payload = new OutboxPreguntaPayload(sqlMetadato.getId(), dto); 
            payloadJson = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            // Manejo de errores de serialización. Esto DEBE fallar la transacción.
            throw new RuntimeException("Error al serializar el evento Outbox: " + e.getMessage());
        }

        // 3b. Guardar el Evento en la tabla Outbox
        Outbox outboxEvent = new Outbox();
        outboxEvent.setTipoEvento("PREGUNTA_CREADA_EVENT");
        outboxEvent.setPayload(payloadJson);
        outboxRepository.save(outboxEvent);

        // Si la inserción en Outbox falla, la inserción de MetadatoPregunta se revierte.
        // NADA es visible externamente (ni en Mongo, ni en SQL) hasta aquí.

        return new ResponseEntity<>(sqlMetadato, HttpStatus.ACCEPTED); // El estado ACCEPTED indica que el procesamiento es asíncrono
        //return new ResponseEntity<>(sqlMetadato, HttpStatus.CREATED);
    }
 // POST /api/v1/admin/contenido/teoria
    @PostMapping("/teoria")
    public ResponseEntity<ContenidoTeoriaResponse> crearContenidoTeoria(@RequestBody ContenidoTeoria contenido) {
        // El servicio se encarga de verificar el idTemaSQL y guardar en Mongo
        ContenidoTeoriaResponse nuevoContenido = contenidoService.saveContenidoTeoria(contenido);
        return new ResponseEntity<>(nuevoContenido, HttpStatus.CREATED);
    }
}