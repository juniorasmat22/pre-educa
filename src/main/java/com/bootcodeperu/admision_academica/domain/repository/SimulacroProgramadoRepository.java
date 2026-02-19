package com.bootcodeperu.admision_academica.domain.repository;

import com.bootcodeperu.admision_academica.domain.model.SimulacroProgramado;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SimulacroProgramadoRepository {
    // Para listar los eventos que aún no terminan y mostrarlos al estudiante
    List<SimulacroProgramado> findByAreaIdAndFechaFinAfter(Long areaId, LocalDateTime fechaActual);

    SimulacroProgramado save(SimulacroProgramado evento);

    Optional<SimulacroProgramado> findById(Long eventoId);
}
