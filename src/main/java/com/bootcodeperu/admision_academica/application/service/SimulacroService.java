package com.bootcodeperu.admision_academica.application.service;

import java.util.List;
import java.util.Map;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.PreguntaDetalle;
import com.bootcodeperu.admision_academica.domain.model.ResultadoSimulacro;

public interface SimulacroService {
	/**
     * Genera un examen simulacro completo para un área de postulación específica.
     *
     * @param areaId El ID del área (A, B, C o D) para la distribución.
     * @return Una lista de PreguntaDetalle (con enunciado, opciones) listas para ser presentadas al usuario.
     */
    List<PreguntaDetalle> generarExamenSimulacro(Long areaId);

    /**
     * Evalúa las respuestas de un usuario y guarda el resultado.
     *
     * @param usuarioId El ID del usuario que completó el examen.
     * @param areaId El ID del área evaluada.
     * @param respuestas Un mapa donde la clave es el mongoIdPregunta y el valor es la respuesta seleccionada.
     * @param tiempoTomado El tiempo que el usuario tardó en completar el examen (en minutos).
     * @return El objeto ResultadoSimulacro guardado con el puntaje final.
     */
    ResultadoSimulacro evaluarSimulacro(Long usuarioId, Long areaId, Map<String, String> respuestas, Integer tiempoTomado);
}
