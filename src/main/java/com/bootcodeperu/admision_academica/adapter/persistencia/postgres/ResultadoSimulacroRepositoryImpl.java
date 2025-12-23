package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringResultadoSimulacroRepository;
import com.bootcodeperu.admision_academica.domain.model.ResultadoSimulacro;
import com.bootcodeperu.admision_academica.domain.repository.ResultadoSimulacroRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ResultadoSimulacroRepositoryImpl implements ResultadoSimulacroRepository {

    private final SpringResultadoSimulacroRepository springResultadoSimulacroRepository;

    @Override
    public ResultadoSimulacro save(ResultadoSimulacro resultado) {
        return springResultadoSimulacroRepository.save(resultado);
    }


    @Override
    public List<ResultadoSimulacro> findByUsuarioId(Long usuarioId) {
        // Llama al método nombrado en Spring Data JPA
        return springResultadoSimulacroRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Optional<ResultadoSimulacro> findTopByUsuarioIdOrderByFechaEvaluacionDesc(Long usuarioId) {
        // Llama al método nombrado para el resultado más reciente
        return springResultadoSimulacroRepository.findTopByUsuarioIdOrderByFechaEvaluacionDesc(usuarioId);
    }
    @Override
    public List<Object[]> findTop10Global(LocalDateTime fecha) {
        return springResultadoSimulacroRepository.findTop10Global(fecha);
    }

    @Override
    public List<Object[]> findTop10ByArea(Long areaId) {
        return springResultadoSimulacroRepository.findTop10ByArea(areaId);
    }
    @Override
    public List<Double> findAllPuntajesByArea(Long areaId) {
        return springResultadoSimulacroRepository.findAllPuntajesByArea(areaId);
    }
    @Override
    public List<ResultadoSimulacro> findTop2ByUsuarioIdOrderByFechaEvaluacionDesc(Long usuarioId) {
        return springResultadoSimulacroRepository.findTop2ByUsuarioIdOrderByFechaEvaluacionDesc(usuarioId);
    }
}
