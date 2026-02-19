package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.analitica.RankingUsuarioResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaDetalleResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.simulacro.SimulacroProgramadoResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface SimulacroProgramadoService {
    SimulacroProgramadoResponse crearSimulacroOficial(Long aLong, String titulo, LocalDateTime localDateTime, LocalDateTime localDateTime1);

    List<SimulacroProgramadoResponse> listarEventosDisponibles(Long areaId);

    List<PreguntaDetalleResponse> iniciarSimulacroOficial(Long usuarioId, Long eventoId);

    List<RankingUsuarioResponse> obtenerRankingPorEvento(Long eventoId);
}
