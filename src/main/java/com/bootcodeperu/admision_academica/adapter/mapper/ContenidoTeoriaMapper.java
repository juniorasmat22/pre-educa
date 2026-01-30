package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.ContenidoTeoria;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
uses = {SeccionContenidoMapper.class})
public interface ContenidoTeoriaMapper {
    ContenidoTeoriaResponse toResponse(ContenidoTeoria contenido);
}
