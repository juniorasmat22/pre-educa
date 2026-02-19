package com.bootcodeperu.admision_academica.application.controller.dto.simulacro;

import java.util.Map;

public record EvaluacionRequest(Long usuarioId, Long areaId, Map<String, String> respuestas, Integer tiempoTomado) {
}