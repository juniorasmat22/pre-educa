package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoResponse;
import com.bootcodeperu.admision_academica.domain.model.Permiso;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermisoMapper {
    PermisoResponse toResponse(Permiso permiso);
}
