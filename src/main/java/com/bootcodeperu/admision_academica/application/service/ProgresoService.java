package com.bootcodeperu.admision_academica.application.service;

import java.util.List;

import com.bootcodeperu.admision_academica.domain.model.ProgresoTema;

public interface ProgresoService {
    
    /**
     * Registra un nuevo resultado de práctica y recalcula el puntaje promedio para el tema.
     * @param usuarioId ID del usuario.
     * @param temaId ID del tema.
     * @param nuevoPuntaje El puntaje obtenido en la última sesión de práctica (Ej: 80.00).
     * @return El objeto ProgresoTema actualizado.
     */
    ProgresoTema actualizarPuntajePromedio(Long usuarioId, Long temaId, Double nuevoPuntaje);
    
    /**
     * Obtiene todo el progreso de un usuario.
     * @param usuarioId ID del usuario.
     * @return Lista de ProgresoTema.
     */
    List<ProgresoTema> getProgresoByUsuario(Long usuarioId);
}