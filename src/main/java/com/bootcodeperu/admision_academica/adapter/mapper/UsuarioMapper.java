package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.application.controller.dto.objetivo.ObjetivoAcademicoResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import com.bootcodeperu.admision_academica.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {RolMapper.class}
)
public interface UsuarioMapper {
    @Mapping(target = "objetivo", ignore = true)
    UsuarioResponse toResponse(Usuario usuario);

    @Mapping(source = "usuario.id", target = "id")
    @Mapping(source = "usuario.nombre", target = "nombre")
    @Mapping(source = "usuario.email", target = "email")
    @Mapping(source = "usuario.rol", target = "rol")
    @Mapping(source = "objetivo", target = "objetivo")
    UsuarioResponse toResponseConObjetivo(Usuario usuario, ObjetivoAcademicoResponse objetivo);
}