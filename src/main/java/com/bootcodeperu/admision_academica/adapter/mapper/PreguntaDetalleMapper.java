package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.PreguntaDetalle;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaDetalleResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaPracticaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.pregunta.OpcionDetalleDTO;
import com.bootcodeperu.admision_academica.application.controller.dto.pregunta.OpcionPracticaDTO;
import com.bootcodeperu.admision_academica.application.controller.dto.pregunta.PreguntaRequest;
import com.bootcodeperu.admision_academica.domain.model.OpcionDetalle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PreguntaDetalleMapper {

    // --- Mapeo a Respuesta Detallada (Admin/Corrección) ---
    PreguntaDetalleResponse toResponse(PreguntaDetalle preguntaDetalle);

    // --- Mapeo a Respuesta de Práctica (Estudiante) ---
    PreguntaPracticaResponse toPractica(PreguntaDetalle preguntaDetalle);

    // --- Mapeos de utilidad para los objetos dentro del Map ---

    // Convierte el modelo de persistencia al DTO con feedback
    OpcionDetalleDTO toOpcionDTO(OpcionDetalle opcionDetalle);

    // Convierte el modelo de persistencia al DTO simple (Oculta el feedback)
    @Mapping(target = "texto", source = "texto")
    OpcionPracticaDTO toOpcionPracticaDTO(OpcionDetalle opcionDetalle);

    // Mapeo inverso: Del Request al Documento (Para guardar en Mongo)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idTemaSQL", source = "idTema")
    PreguntaDetalle toDocument(PreguntaRequest request);
}