package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.SeccionContenido;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.SeccionContenidoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeccionContenidoMapper {
    SeccionContenidoResponse toResponse(SeccionContenido seccion);
}
