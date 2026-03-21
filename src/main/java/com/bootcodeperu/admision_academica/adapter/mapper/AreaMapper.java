package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaResponse;
import com.bootcodeperu.admision_academica.domain.model.Area;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AreaMapper {
    @Mapping(source = "universidad.id", target = "idUniversidad")
    @Mapping(source = "universidad.nombre", target = "nombreUniversidad")
    AreaResponse toResponse(Area area);

    Area toEntity(AreaRequest request);
}
