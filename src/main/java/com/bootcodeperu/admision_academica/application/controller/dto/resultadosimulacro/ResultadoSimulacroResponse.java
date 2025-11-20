package com.bootcodeperu.admision_academica.application.controller.dto.resultadosimulacro;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;

public record ResultadoSimulacroResponse(
		Long id,
	    Long idUsuario,
	    Long idAreaEvaluada,
	    LocalDateTime fechaEvaluacion,
	    Integer tiempoTomado,
	    Double puntajeTotal,
	    JsonNode detallesRespuestas) {

}
