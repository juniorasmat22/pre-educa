package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CursoAreaResponse;
import com.bootcodeperu.admision_academica.domain.model.CursoArea;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { AreaMapper.class, CursoMapper.class })
public interface CursoAreaMapper {
    CursoAreaResponse toResponse(CursoArea cursoArea);
}
