package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.enums.EstadoSimulacro;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcodeperu.admision_academica.domain.model.ResultadoSimulacro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringResultadoSimulacroRepository extends JpaRepository<ResultadoSimulacro, Long> {

    // Spring Data JPA lo implementa automáticamente basado en el nombre del método:
    List<ResultadoSimulacro> findByUsuarioId(Long usuarioId);

    // Spring Data JPA lo implementa automáticamente para obtener el más reciente:
    Optional<ResultadoSimulacro> findTopByUsuarioIdOrderByFechaEvaluacionDesc(Long usuarioId);

    @Query(value = "SELECT u.nombre, MAX(r.puntaje_total) as maxPuntaje, u.carrera_deseada " +
            "FROM resultado_simulacro r " +
            "JOIN usuario u ON r.id_usuario = u.id " +
            "WHERE r.id_area_evaluada = :areaId " +
            "GROUP BY u.id, u.nombre, u.carrera_deseada " +
            "ORDER BY maxPuntaje DESC LIMIT 10", nativeQuery = true)
    List<Object[]> findTop10ByArea(@Param("areaId") Long areaId);


    @Query("SELECT r.usuario.nombre, MAX(r.puntajeTotal) as maxPuntaje, r.usuario.carreraDeseada " +
            "FROM ResultadoSimulacro r " +
            "WHERE r.fechaEvaluacion >= :fecha " +
            "AND r.simulacroProgramado IS NULL " +
            "GROUP BY r.usuario.id, r.usuario.nombre, r.usuario.carreraDeseada " +
            "ORDER BY maxPuntaje DESC LIMIT 10")
    List<Object[]> findTop10GlobalLibres(@Param("fecha") LocalDateTime fecha);

    @Query("SELECT r.puntajeTotal FROM ResultadoSimulacro r " +
            "WHERE r.areaEvaluada.id = :areaId " +
            "AND r.simulacroProgramado IS NULL " + // <-- NUEVO
            "ORDER BY r.puntajeTotal ASC")
    List<Double> findAllPuntajesLibresByArea(@Param("areaId") Long areaId);

    // Genera: SELECT * FROM resultado_simulacro WHERE id_usuario = ? ORDER BY fecha_evaluacion DESC LIMIT 2
    List<ResultadoSimulacro> findTop2ByUsuarioIdOrderByFechaEvaluacionDesc(Long usuarioId);

    // Para Control de Intentos
    boolean existsByUsuarioIdAndEstado(Long usuarioId, EstadoSimulacro estado);

    // Para el Scheduler: Busca exámenes vencidos que aún no se han cerrado
    List<ResultadoSimulacro> findByEstadoAndFechaExpiracionBefore(EstadoSimulacro estado, LocalDateTime fecha);

    @Query(value = """
            SELECT 
                u.nombre as nombre,
                r.puntaje_total as puntaje,
                a.nombre as carrera,
                DENSE_RANK() OVER (
                    PARTITION BY r.id_area 
                    ORDER BY r.puntaje_total DESC, r.preguntas_incorrectas ASC, r.tiempo_tomado ASC
                ) as puesto
            FROM resultadosimulacro r
            JOIN usuario u ON r.id_usuario = u.id
            JOIN area a ON r.id_area = a.id
            WHERE r.id_area = :areaId 
            AND r.estado != 'EN_CURSO'
            AND r.simulacro_programado_id IS NULL -- <-- NUEVO
            ORDER BY puesto ASC
            LIMIT 10
            """, nativeQuery = true)
    List<Object[]> findRankingLibreByArea(Long areaId);

    Optional<ResultadoSimulacro> findByUsuarioIdAndEstado(Long usuarioId, EstadoSimulacro estado);

    /*metodospara simulacros programador*/
    boolean existsByUsuarioIdAndSimulacroProgramadoId(Long usuarioId, Long eventoId);

    @Query(value = """
            SELECT 
                u.nombre as nombre,
                r.puntaje_total as puntaje,
                a.nombre as carrera,
                DENSE_RANK() OVER (
                    ORDER BY r.puntaje_total DESC, r.preguntas_incorrectas ASC, r.tiempo_tomado ASC
                ) as puesto
            FROM resultadosimulacro r
            JOIN usuario u ON r.id_usuario = u.id
            JOIN area a ON r.id_area = a.id
            WHERE r.simulacro_programado_id = :eventoId AND r.estado = 'FINALIZADO'
            ORDER BY puesto ASC
            LIMIT 100
            """, nativeQuery = true)
    List<Object[]> findRankingOficialByEvento(Long eventoId);

    // 1. TOP 10 GLOBAL DEL EVENTO (Todas las áreas mezcladas)
    @Query(value = """
            SELECT u.nombre as nombre, r.puntaje_total as puntaje, a.nombre as carrera,
                   DENSE_RANK() OVER (ORDER BY r.puntaje_total DESC, r.preguntas_incorrectas ASC, r.tiempo_tomado ASC) as puesto
            FROM resultadosimulacro r
            JOIN usuario u ON r.id_usuario = u.id
            JOIN area a ON r.id_area = a.id
            WHERE r.simulacro_programado_id = :eventoId AND r.estado = 'FINALIZADO'
            ORDER BY puesto ASC LIMIT 10
            """, nativeQuery = true)
    List<Object[]> findTop10GlobalByEvento(Long eventoId);

    // 2. RANKING COMPLETO GLOBAL DEL EVENTO (Todos los participantes)
    @Query(value = """
            SELECT u.nombre as nombre, r.puntaje_total as puntaje, a.nombre as carrera,
                   DENSE_RANK() OVER (ORDER BY r.puntaje_total DESC, r.preguntas_incorrectas ASC, r.tiempo_tomado ASC) as puesto
            FROM resultadosimulacro r
            JOIN usuario u ON r.id_usuario = u.id
            JOIN area a ON r.id_area = a.id
            WHERE r.simulacro_programado_id = :eventoId AND r.estado = 'FINALIZADO'
            ORDER BY puesto ASC
            """, nativeQuery = true)
    List<Object[]> findRankingGlobalCompletoByEvento(Long eventoId);

    // 3. TOP 10 POR ÁREA ESPECÍFICA EN EL EVENTO
    @Query(value = """
            SELECT u.nombre as nombre, r.puntaje_total as puntaje, a.nombre as carrera,
                   DENSE_RANK() OVER (ORDER BY r.puntaje_total DESC, r.preguntas_incorrectas ASC, r.tiempo_tomado ASC) as puesto
            FROM resultadosimulacro r
            JOIN usuario u ON r.id_usuario = u.id
            JOIN area a ON r.id_area = a.id
            WHERE r.simulacro_programado_id = :eventoId AND r.id_area = :areaId AND r.estado = 'FINALIZADO'
            ORDER BY puesto ASC LIMIT 10
            """, nativeQuery = true)
    List<Object[]> findTop10ByAreaAndEvento(Long eventoId, Long areaId);

    // 4. RANKING COMPLETO POR ÁREA EN EL EVENTO
    @Query(value = """
            SELECT u.nombre as nombre, r.puntaje_total as puntaje, a.nombre as carrera,
                   DENSE_RANK() OVER (ORDER BY r.puntaje_total DESC, r.preguntas_incorrectas ASC, r.tiempo_tomado ASC) as puesto
            FROM resultadosimulacro r
            JOIN usuario u ON r.id_usuario = u.id
            JOIN area a ON r.id_area = a.id
            WHERE r.simulacro_programado_id = :eventoId AND r.id_area = :areaId AND r.estado = 'FINALIZADO'
            ORDER BY puesto ASC
            """, nativeQuery = true)
    List<Object[]> findRankingCompletoByAreaAndEvento(Long eventoId, Long areaId);

    //REPORTE DE ESTADÍSTICAS DEL EVENTO (Asistencia, Promedios y Tiempos)
    @Query(value = """
            SELECT 
                COUNT(r.id) as total_inscritos,
                SUM(CASE WHEN r.estado = 'FINALIZADO' THEN 1 ELSE 0 END) as total_finalizados,
                SUM(CASE WHEN r.estado = 'EXPIRADO' THEN 1 ELSE 0 END) as total_abandonos,
                COALESCE(AVG(CASE WHEN r.estado = 'FINALIZADO' THEN r.puntaje_total ELSE NULL END), 0) as puntaje_promedio,
                COALESCE(MAX(CASE WHEN r.estado = 'FINALIZADO' THEN r.puntaje_total ELSE NULL END), 0) as puntaje_maximo,
                COALESCE(MIN(CASE WHEN r.estado = 'FINALIZADO' THEN r.puntaje_total ELSE NULL END), 0) as puntaje_minimo,
                COALESCE(AVG(CASE WHEN r.estado = 'FINALIZADO' THEN r.tiempo_tomado ELSE NULL END), 0) as tiempo_promedio
            FROM resultadosimulacro r
            WHERE r.simulacro_programado_id = :eventoId
            """, nativeQuery = true)
    Object[] obtenerMetricasGlobalesDelEvento(Long eventoId);
}
