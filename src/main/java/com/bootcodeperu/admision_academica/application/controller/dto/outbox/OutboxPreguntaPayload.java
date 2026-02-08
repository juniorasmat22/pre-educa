package com.bootcodeperu.admision_academica.application.controller.dto.outbox;

import com.bootcodeperu.admision_academica.application.controller.dto.PreguntaCreationDTO;

//Usamos record o DTO inmutable
public record OutboxPreguntaPayload(
        Long sqlMetadatoId, // El ID de la tabla MetadatoPregunta (PostgreSQL)
        PreguntaCreationDTO data // El DTO de la pregunta con todos los datos
) {
}
