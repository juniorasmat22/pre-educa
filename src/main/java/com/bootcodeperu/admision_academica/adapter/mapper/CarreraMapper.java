package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.carrera.CarreraResponse;
import com.bootcodeperu.admision_academica.domain.model.Carrera;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarreraMapper {

    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "area.nombre", target = "areaNombre")
    CarreraResponse toResponse(Carrera carrera);
}