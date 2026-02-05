package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.tema.TemaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.tema.TemaResponseDetalle;
import com.bootcodeperu.admision_academica.domain.model.Tema;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {CursoMapper.class}
)
public interface TemaMapper {
    TemaResponse toResponse(Tema tema);
    @Mapping(source = "curso.id", target = "cursoId")
    @Mapping(source = "curso.nombre", target = "nombreCurso")
    TemaResponseDetalle toResponseDetalle(Tema tema);
}
