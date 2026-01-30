package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.tema.TemaResponse;
import com.bootcodeperu.admision_academica.domain.model.Tema;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {CursoMaper.class}
)
public interface TemaMapper {
    TemaResponse toResponse(Tema tema);
}
