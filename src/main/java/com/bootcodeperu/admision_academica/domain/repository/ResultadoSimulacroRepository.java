package com.bootcodeperu.admision_academica.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.ResultadoSimulacro;
import org.springframework.data.repository.query.Param;

public interface ResultadoSimulacroRepository {
	// Método de negocio 1: Guardar el resultado de un examen completado.
    ResultadoSimulacro save(ResultadoSimulacro resultado);

    // Método de negocio 2: Obtener historial de simulacros de un usuario.
    List<ResultadoSimulacro> findByUsuarioId(Long usuarioId);

    // Método de negocio 3: Obtener el último resultado de un usuario para una métrica rápida.
    Optional<ResultadoSimulacro> findTopByUsuarioIdOrderByFechaEvaluacionDesc(Long usuarioId);
    // Métodos para el Ranking (sin @Query aquí)
    List<Object[]> findTop10Global(LocalDateTime fecha);
    List<Object[]> findTop10ByArea(Long areaId);
    List<Double> findAllPuntajesByArea(@Param("areaId") Long areaId);
}
