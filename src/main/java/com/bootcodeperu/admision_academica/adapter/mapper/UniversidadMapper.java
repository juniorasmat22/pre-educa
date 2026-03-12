package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.universidad.UniversidadRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.universidad.UniversidadResponse;
import com.bootcodeperu.admision_academica.domain.model.Universidad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UniversidadMapper {

    UniversidadResponse toResponse(Universidad universidad);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "areas", ignore = true)
    Universidad toEntity(UniversidadRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "areas", ignore = true)
    void updateEntity(UniversidadRequest request, @MappingTarget Universidad universidad);
}