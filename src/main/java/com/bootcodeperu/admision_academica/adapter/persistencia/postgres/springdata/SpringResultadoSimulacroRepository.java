package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcodeperu.admision_academica.domain.model.ResultadoSimulacro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringResultadoSimulacroRepository extends JpaRepository<ResultadoSimulacro, Long> {

    // Spring Data JPA lo implementa automáticamente basado en el nombre del método:
    List<ResultadoSimulacro> findByUsuarioId(Long usuarioId);

    // Spring Data JPA lo implementa automáticamente para obtener el más reciente:
    Optional<ResultadoSimulacro> findTopByUsuarioIdOrderByFechaEvaluacionDesc(Long usuarioId);
    // Archivo: SpringResultadoSimulacroRepository.java
    @Query(value = "SELECT u.nombre, MAX(r.puntaje_total) as maxPuntaje, u.carrera_deseada " +
            "FROM resultado_simulacro r " +
            "JOIN usuario u ON r.id_usuario = u.id " +
            "WHERE r.id_area_evaluada = :areaId " +
            "GROUP BY u.id, u.nombre, u.carrera_deseada " +
            "ORDER BY maxPuntaje DESC LIMIT 10", nativeQuery = true)
    List<Object[]> findTop10ByArea(@Param("areaId") Long areaId);

    // Archivo: src/main/java/.../adapter/persistencia/postgres/springdata/SpringResultadoSimulacroRepository.java
    @Query("SELECT r.usuario.nombre, MAX(r.puntajeTotal) as maxPuntaje, r.usuario.carreraDeseada " +
            "FROM ResultadoSimulacro r " +
            "WHERE r.fechaEvaluacion >= :fecha " +
            "GROUP BY r.usuario.id, r.usuario.nombre, r.usuario.carreraDeseada " +
            "ORDER BY maxPuntaje DESC")
    List<Object[]> findTop10Global(@Param("fecha") LocalDateTime fecha);

}
