package com.bootcodeperu.admision_academica.adapter.mapper;


import com.bootcodeperu.admision_academica.application.controller.dto.resultadosimulacro.ResultadoSimulacroResponse;
import com.bootcodeperu.admision_academica.domain.model.ResultadoSimulacro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ResultadoSimulacroMapper {

    @Mapping(source = "usuario.id", target = "idUsuario")
    @Mapping(source = "areaEvaluada.id", target = "idAreaEvaluada")
    ResultadoSimulacroResponse toResponse(ResultadoSimulacro entity);
}