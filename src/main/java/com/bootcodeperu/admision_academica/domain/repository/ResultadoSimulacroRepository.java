package com.bootcodeperu.admision_academica.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.ResultadoSimulacro;
import com.bootcodeperu.admision_academica.domain.model.enums.EstadoSimulacro;
import org.springframework.data.repository.query.Param;

public interface ResultadoSimulacroRepository {
    // Método de negocio 1: Guardar el resultado de un examen completado.
    ResultadoSimulacro save(ResultadoSimulacro resultado);

    // Método de negocio 2: Obtener historial de simulacros de un usuario.
    List<ResultadoSimulacro> findByUsuarioId(Long usuarioId);

    // Método de negocio 3: Obtener el último resultado de un usuario para una métrica rápida.
    Optional<ResultadoSimulacro> findTopByUsuarioIdOrderByFechaEvaluacionDesc(Long usuarioId);

    // Métodos para el Ranking (sin @Query aquí)
    List<Object[]> findTop10GlobalLibres(LocalDateTime fecha);

    List<Object[]> findTop10ByArea(Long areaId);

    List<Double> findAllPuntajesLibresByArea(@Param("areaId") Long areaId);

    //Devuelve los 2 últimos simulacros para comparar tendencias
    List<ResultadoSimulacro> findTop2ByUsuarioIdOrderByFechaEvaluacionDesc(Long usuarioId);

    boolean existsByUsuarioIdAndEstado(Long usuarioId, EstadoSimulacro estado);

    List<ResultadoSimulacro> findByEstadoAndFechaExpiracionBefore(EstadoSimulacro estado, LocalDateTime fecha);

    List<Object[]> findRankingLibreByArea(Long areaId);

    Optional<ResultadoSimulacro> findByUsuarioIdAndEstado(Long usuarioId, EstadoSimulacro estado);

    boolean existsByUsuarioIdAndSimulacroProgramadoId(Long usuarioId, Long eventoId);

    List<Object[]> findRankingOficialByEvento(Long eventoId);

    List<Object[]> findRankingGlobalCompletoByEvento(Long eventoId);

    List<Object[]> findTop10GlobalByEvento(Long eventoId);

    List<Object[]> findTop10ByAreaAndEvento(Long eventoId, Long areaId);

    List<Object[]> findRankingCompletoByAreaAndEvento(Long eventoId, Long areaId);

    Object[] obtenerMetricasGlobalesDelEvento(Long eventoId);
}
