package com.bootcodeperu.admision_academica.application.service;

import java.util.List;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.ContenidoTeoria;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaDetalleResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.tema.TemaResponse;
import com.bootcodeperu.admision_academica.domain.model.ProgresoTema;

public interface ContenidoService {
	/**
     * Obtiene todos los temas de un curso.
     * @param cursoId ID del curso.
     * @return Lista de Temas.
     */
    List<TemaResponse> getTemasByCursoId(Long cursoId);

    /**
     * Obtiene un conjunto de preguntas para práctica, filtradas por tema y nivel.
     *
     * @param temaId ID del tema.
     * @param nivel Nivel de dificultad ('Básico', 'Intermedio', 'Avanzado').
     * @return Lista de PreguntaDetalle.
     */
    List<PreguntaDetalleResponse> getPreguntasPractica(Long temaId, String nivel);

    /**
     * Marca la teoría de un tema como completada y actualiza el progreso.
     * @param usuarioId ID del usuario.
     * @param temaId ID del tema.
     * @return El objeto ProgresoTema actualizado.
     */
    ProgresoTema completarTeoria(Long usuarioId, Long temaId);
    
    /** NUEVO MÉTODO **/
    List<ContenidoTeoriaResponse> getContenidoTeoriaByTemaId(Long temaId);

    /** NUEVO MÉTODO (para administración) **/
    ContenidoTeoria saveContenidoTeoria(ContenidoTeoria contenido);
}
