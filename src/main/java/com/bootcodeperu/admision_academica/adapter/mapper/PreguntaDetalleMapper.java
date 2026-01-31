package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.PreguntaDetalle;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaDetalleResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaPracticaResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PreguntaDetalleMapper {
    PreguntaDetalleResponse toResponse(PreguntaDetalle preguntaDetalle);
    PreguntaPracticaResponse toPractica(PreguntaDetalle preguntaDetalle);
}