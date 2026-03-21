package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.objetivo.ObjetivoAcademicoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.objetivo.ObjetivoAcademicoResponse;
import com.bootcodeperu.admision_academica.domain.model.ObjetivoAcademico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ObjetivoAcademicoMapper {

    @Mapping(source = "usuario.id", target = "idUsuario")
    @Mapping(source = "usuario.nombre", target = "nombreUsuario")
    @Mapping(source = "carrera.id", target = "idCarrera")
    @Mapping(source = "carrera.nombre", target = "nombreCarrera")
    @Mapping(source = "procesoAdmision.id", target = "idProcesoAdmision")
    @Mapping(source = "procesoAdmision.nombre", target = "nombreProceso")
    ObjetivoAcademicoResponse toResponse(ObjetivoAcademico objetivo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "carrera", ignore = true)
    @Mapping(target = "procesoAdmision", ignore = true)
    void updateEntity(ObjetivoAcademicoRequest request, @MappingTarget ObjetivoAcademico objetivo);
}