package com.bootcodeperu.admision_academica.application.controller.dto.tema;

public record TemaResponseDetalle(Long id,
                                  String nombreTema,
                                  Long cursoId,
                                  String nombreCurso) {
}
