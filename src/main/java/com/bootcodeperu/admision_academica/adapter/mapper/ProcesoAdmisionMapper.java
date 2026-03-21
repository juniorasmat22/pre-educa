package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.procesoadmision.ProcesoAdmisionRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.procesoadmision.ProcesoAdmisionResponse;
import com.bootcodeperu.admision_academica.domain.model.ProcesoAdmision;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProcesoAdmisionMapper {

    @Mapping(source = "universidad.id", target = "idUniversidad")
    @Mapping(source = "universidad.nombre", target = "nombreUniversidad")
    ProcesoAdmisionResponse toResponse(ProcesoAdmision proceso);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "universidad", ignore = true)
    ProcesoAdmision toEntity(ProcesoAdmisionRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "universidad", ignore = true)
    void updateEntity(ProcesoAdmisionRequest request, @MappingTarget ProcesoAdmision proceso);
}
