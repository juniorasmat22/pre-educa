package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.ContenidoTeoria;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.SeccionContenido;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.SeccionContenidoResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",
uses = {SeccionContenidoMapper.class})
public interface ContenidoTeoriaMapper {
    ContenidoTeoriaResponse toResponse(ContenidoTeoria contenido);
    List<SeccionContenido> toResponseList(List<SeccionContenidoResponse> entities);
}
