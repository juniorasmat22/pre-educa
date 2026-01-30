package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolResponse;
import com.bootcodeperu.admision_academica.domain.model.Rol;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = { PermisoMapper.class } // <--- AQUÍ conectamos los mappers
)
public interface RolMapper {
    // MapStruct entiende que RolResponse es un Record y usará su constructor
    RolResponse toRolResponse(Rol rol);
}
