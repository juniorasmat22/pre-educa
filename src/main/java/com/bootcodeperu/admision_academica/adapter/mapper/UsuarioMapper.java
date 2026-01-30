package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import com.bootcodeperu.admision_academica.domain.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = { RolMapper.class }
)
public interface UsuarioMapper {
    UsuarioResponse toResponse(Usuario usuario);
}