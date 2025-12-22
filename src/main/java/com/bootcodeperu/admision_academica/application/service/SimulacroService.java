package com.bootcodeperu.admision_academica.application.service;

import java.util.List;
import java.util.Map;

import com.bootcodeperu.admision_academica.application.controller.dto.analitica.DebilidadTemaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.analitica.RankingUsuarioResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaDetalleResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.resultadosimulacro.ResultadoSimulacroResponse;

public interface SimulacroService {
	/**
     * Genera un examen simulacro completo para un área de postulación específica.
     *
     * @param areaId El ID del área (A, B, C o D) para la distribución.
     * @return Una lista de PreguntaDetalle (con enunciado, opciones) listas para ser presentadas al usuario.
     */
    List<PreguntaDetalleResponse> generarExamenSimulacro(Long areaId);

    /**
     * Evalúa las respuestas de un usuario y guarda el resultado.
     *
     * @param usuarioId El ID del usuario que completó el examen.
     * @param areaId El ID del área evaluada.
     * @param respuestas Un mapa donde la clave es el mongoIdPregunta y el valor es la respuesta seleccionada.
     * @param tiempoTomado El tiempo que el usuario tardó en completar el examen (en minutos).
     * @return El objeto ResultadoSimulacroResponse guardado con el puntaje final.
     */
    ResultadoSimulacroResponse evaluarSimulacro(Long usuarioId, Long areaId, Map<String, String> respuestas, Integer tiempoTomado);
    List<DebilidadTemaResponse> obtenerAnalisisDebilidades(Long usuarioId);
    List<RankingUsuarioResponse> obtenerTop10GlobalSemanal();
    List<RankingUsuarioResponse> obtenerRankingPorArea(Long areaId);
}
