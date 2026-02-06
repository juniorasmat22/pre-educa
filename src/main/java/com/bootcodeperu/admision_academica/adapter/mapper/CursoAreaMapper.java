package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CursoAreaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CursoAreaSimpleResponse;
import com.bootcodeperu.admision_academica.domain.model.CursoArea;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { AreaMapper.class, CursoMapper.class })
public interface CursoAreaMapper {
    CursoAreaResponse toResponse(CursoArea cursoArea);
    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "area.nombre", target = "areaNombre")
    @Mapping(source = "curso.id", target = "cursoId")
    @Mapping(source = "curso.nombre", target = "cursoNombre")
    CursoAreaSimpleResponse toSimpleResponse(CursoArea cursoArea);
}
