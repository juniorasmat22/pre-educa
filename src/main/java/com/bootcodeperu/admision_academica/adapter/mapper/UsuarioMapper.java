package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import com.bootcodeperu.admision_academica.domain.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = { SeguridadMapper.class }
)
public interface UsuarioMapper {

    // MapStruct mapea automáticamente campos con el mismo nombre.
    // rol -> rol (y usa SeguridadMapper internamente)
    UsuarioResponse toResponse(Usuario usuario);
}