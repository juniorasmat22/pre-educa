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
            "GROUP BY r.usuario.id, r.usuario.nombre, r.usuario.carreraDeseada " +
            "ORDER BY maxPuntaje DESC")
    List<Object[]> findTop10Global(@Param("fecha") LocalDateTime fecha);

    @Query("SELECT r.puntajeTotal FROM ResultadoSimulacro r " +
            "WHERE r.areaEvaluada.id = :areaId " +
            "ORDER BY r.puntajeTotal ASC")
    List<Double> findAllPuntajesByArea(@Param("areaId") Long areaId);

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
                    ORDER BY 
                        r.puntaje_total DESC, 
                        r.preguntas_incorrectas ASC, 
                        r.tiempo_tomado ASC
                ) as puesto
            FROM resultadosimulacro r
            JOIN usuario u ON r.id_usuario = u.id
            JOIN area a ON r.id_area = a.id
            WHERE r.id_area= :areaId AND r.estado != 'EN_CURSO'
            ORDER BY puesto ASC
            LIMIT 10
            """, nativeQuery = true)
    List<Object[]> findRankingOficialByArea(Long areaId);

    Optional<ResultadoSimulacro> findByUsuarioIdAndEstado(Long usuarioId, EstadoSimulacro estado);

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
}
