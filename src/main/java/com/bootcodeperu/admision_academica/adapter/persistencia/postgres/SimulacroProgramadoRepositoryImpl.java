package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringResultadoSimulacroRepository;
import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringSimulacroProgramadoRepository;
import com.bootcodeperu.admision_academica.domain.model.SimulacroProgramado;
import com.bootcodeperu.admision_academica.domain.repository.SimulacroProgramadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository   // También puede ser @Component
@RequiredArgsConstructor
public class SimulacroProgramadoRepositoryImpl implements SimulacroProgramadoRepository {
    private final SpringSimulacroProgramadoRepository simulacroProgramadoRepository;

    @Override
    public List<SimulacroProgramado> findByAreaIdAndFechaFinAfter(Long areaId, LocalDateTime fechaActual) {
        return simulacroProgramadoRepository.findByAreaIdAndFechaFinAfter(areaId, fechaActual);
    }

    @Override
    public SimulacroProgramado save(SimulacroProgramado evento) {
        return simulacroProgramadoRepository.save(evento);
    }

    @Override
    public Optional<SimulacroProgramado> findById(Long eventoId) {
        return simulacroProgramadoRepository.findById(eventoId);
    }
}
