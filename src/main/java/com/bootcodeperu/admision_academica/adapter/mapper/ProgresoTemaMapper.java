package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.progresotema.ProgresoTemaResponse;
import com.bootcodeperu.admision_academica.domain.model.ProgresoTema;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel ="spring",
uses = {TemaMapper.class}
)
public interface ProgresoTemaMapper {
    @Mapping(source = "usuario.id", target = "idUsuario")
    ProgresoTemaResponse toResponse(ProgresoTema progresoTema);
}
