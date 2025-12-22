package com.bootcodeperu.admision_academica.application.controller.dto.simulacro;

import com.bootcodeperu.admision_academica.application.controller.dto.analitica.DebilidadTemaResponse;

import java.util.List;

public record SimulacroResultResponse(
        Double puntajeTotal,
        Integer correctas,
        Integer incorrectas,
        Integer enBlanco,
        List<DebilidadTemaResponse> temasAReforzar
) {}