package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bootcodeperu.admision_academica.domain.model.MetadatoPregunta;

public interface SpringMetadatoPreguntaRepository extends JpaRepository<MetadatoPregunta, Long> {

    // Método 1: Obtener preguntas de práctica por Tema y Nivel.
    List<MetadatoPregunta> findByTemaIdAndNivel(Long temaId, String nivel);
    
    // Método 2: Contar preguntas de BANCO por curso/área (utiliza los IDs de tema del curso).
    Long countByTemaIdInAndTipoPregunta(List<Long> temaIds, String tipoPregunta);
    
    // Método 3: Seleccionar un conjunto ALEATORIO de preguntas de BANCO para un simulacro.
    @Query(value = "SELECT * FROM metadatopregunta m " +
                   "WHERE m.id_tema IN (:temaIds) AND m.tipo_pregunta = :tipoPregunta " +
                   "ORDER BY RANDOM() LIMIT :limit",
           nativeQuery = true) // Usamos nativeQuery para la función RANDOM() de PostgreSQL
    List<MetadatoPregunta> findRandomByTemaIdInAndTipoPregunta(
            @Param("temaIds") List<Long> temaIds,
            @Param("tipoPregunta") String tipoPregunta,
            @Param("limit") int limit
    );

    // Método 4: Encontrar por el ID de Mongo.
    Optional<MetadatoPregunta> findByMongoIdPregunta(String mongoIdPregunta);
}