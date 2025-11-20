package com.bootcodeperu.admision_academica.domain.repository;

import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.MetadatoPregunta;

public interface MetadatoPreguntaRepository {
	// CRUD básico
    MetadatoPregunta save(MetadatoPregunta metadato);

    // Método de negocio 1: Obtener preguntas para práctica por TEMA y NIVEL.
    List<MetadatoPregunta> findByTemaIdAndNivel(Long temaId, String nivel);

    // Método de negocio 2: Contar preguntas de BANCO por curso/área para generar simulacros.
    // Necesitas el ID de los temas que pertenecen a un curso, por eso recibe una lista de IDs.
    Long countByTemaIdInAndTipoPregunta(List<Long> temaIds, String tipoPregunta);

    // Método de negocio 3: Seleccionar un conjunto ALEATORIO de preguntas de BANCO para un simulacro.
    // El 'tipoPregunta' será 'BancoSimulacro'.
    List<MetadatoPregunta> findRandomByTemaIdInAndTipoPregunta(
        List<Long> temaIds, 
        String tipoPregunta, 
        int limit // La cantidad de preguntas que requiere el examen de ese curso.
    );
    
    // Método de negocio 4: Encontrar por el ID de Mongo para el detalle de la pregunta.
    Optional<MetadatoPregunta> findByMongoIdPregunta(String mongoId);
    
 //  Buscar por la Clave Primaria de PostgreSQL
    Optional<MetadatoPregunta> findById(Long id); 
}
