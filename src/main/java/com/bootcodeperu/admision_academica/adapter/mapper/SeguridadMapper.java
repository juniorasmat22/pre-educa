package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.permiso.PermisoResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.rol.RolResponse;
import com.bootcodeperu.admision_academica.domain.model.Permiso;
import com.bootcodeperu.admision_academica.domain.model.Rol;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeguridadMapper {
    // MapStruct entiende que RolResponse es un Record y usará su constructor
    RolResponse toRolResponse(Rol rol);

    // Mapeo automático para la colección de permisos
    PermisoResponse toPermisoResponse(Permiso permiso);
}
