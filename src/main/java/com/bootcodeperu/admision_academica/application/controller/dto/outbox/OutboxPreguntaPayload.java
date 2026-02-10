package com.bootcodeperu.admision_academica.application.controller.dto.outbox;

import com.bootcodeperu.admision_academica.application.controller.dto.pregunta.PreguntaRequest;

//Usamos record o DTO inmutable
public record OutboxPreguntaPayload(
        Long sqlMetadatoId, // El ID de la tabla MetadatoPregunta (PostgreSQL)
        PreguntaRequest data // El DTO de la pregunta con todos los datos
) {
}
