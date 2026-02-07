package com.bootcodeperu.admision_academica.application.service;

import java.util.List;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.ContenidoTeoria;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaPracticaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.progresotema.ProgresoTemaResponse;
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
    List<PreguntaPracticaResponse> getPreguntasPractica(Long temaId, String nivel);

    /**
     * Marca la teoría de un tema como completada y actualiza el progreso.
     * @param usuarioId ID del usuario.
     * @param temaId ID del tema.
     * @return El objeto ProgresoTema actualizado.
     */
    ProgresoTemaResponse completarTeoria(Long usuarioId, Long temaId);
    
    /** NUEVO MÉTODO **/
    List<ContenidoTeoriaResponse> getContenidoTeoriaByTemaId(Long temaId);

    /** NUEVO MÉTODO (para administración) **/
    ContenidoTeoriaResponse saveContenidoTeoria(ContenidoTeoria contenido);
    ContenidoTeoriaResponse updateContenidoTeoria(Long temaId, String contenidoId, ContenidoTeoriaRequest request);
    void deleteContenidoTeoria(Long temaId, String contenidoId);
}
