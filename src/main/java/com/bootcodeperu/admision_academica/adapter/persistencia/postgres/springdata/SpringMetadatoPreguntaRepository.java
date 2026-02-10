package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.enums.QuestionTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bootcodeperu.admision_academica.domain.model.MetadatoPregunta;

public interface SpringMetadatoPreguntaRepository extends JpaRepository<MetadatoPregunta, Long> {

    // Método 1: Obtener preguntas de práctica por Tema y Nivel.
    List<MetadatoPregunta> findByTemaIdAndNivel(Long temaId, String nivel);

    // Método optimizado: Filtra por Tema, Nivel y el Objetivo (Practice, Flashcard, etc.)
    List<MetadatoPregunta> findByTemaIdAndNivelAndTarget(Long temaId, String nivel, QuestionTarget target);

    // Si para las Flashcards no te importa el nivel, puedes tener este otro:
    List<MetadatoPregunta> findByTemaIdAndTarget(Long temaId, QuestionTarget target);

    // Método 2: Contar preguntas de BANCO por curso/área (utiliza los IDs de tema del curso).
    Long countByTemaIdInAndTarget(List<Long> temaIds, QuestionTarget target);

    // Método 3: Seleccionar un conjunto ALEATORIO de preguntas de BANCO para un simulacro.
    @Query(value = "SELECT * FROM metadatopregunta m " +
            "WHERE m.id_tema IN (:temaIds) AND m.target = :target " +
            "ORDER BY RANDOM() LIMIT :limit",
            nativeQuery = true)
    // Usamos nativeQuery para la función RANDOM() de PostgreSQL
    List<MetadatoPregunta> findRandomByTemaIdInAndTarget(
            @Param("temaIds") List<Long> temaIds,
            @Param("target") String target,
            @Param("limit") int limit
    );

    // Método 4: Encontrar por el ID de Mongo.
    Optional<MetadatoPregunta> findByMongoIdPregunta(String mongoIdPregunta);
}