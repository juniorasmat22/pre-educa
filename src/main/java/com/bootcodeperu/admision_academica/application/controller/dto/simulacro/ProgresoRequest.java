package com.bootcodeperu.admision_academica.application.controller.dto.simulacro;

import java.util.Map;

public record ProgresoRequest(Long usuarioId, Map<String, String> respuestas) {
}
