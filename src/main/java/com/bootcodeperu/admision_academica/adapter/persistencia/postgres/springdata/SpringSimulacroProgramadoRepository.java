package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import com.bootcodeperu.admision_academica.domain.model.SimulacroProgramado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringSimulacroProgramadoRepository extends JpaRepository<SimulacroProgramado, Long> {
    List<SimulacroProgramado> findByAreaIdAndFechaFinAfter(Long areaId, LocalDateTime fechaActual);
}
