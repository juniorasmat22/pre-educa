package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.curso.CursoResponse;
import com.bootcodeperu.admision_academica.domain.model.Curso;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CursoMaper {
    CursoResponse toResponse(Curso curso);
}
