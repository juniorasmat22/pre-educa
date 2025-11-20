package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcodeperu.admision_academica.domain.model.ResultadoSimulacro;

public interface SpringResultadoSimulacroRepository extends JpaRepository<ResultadoSimulacro, Long> {

    // Spring Data JPA lo implementa automáticamente basado en el nombre del método:
    List<ResultadoSimulacro> findByUsuarioId(Long usuarioId);

    // Spring Data JPA lo implementa automáticamente para obtener el más reciente:
    Optional<ResultadoSimulacro> findTopByUsuarioIdOrderByFechaEvaluacionDesc(Long usuarioId);
}
