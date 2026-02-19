package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.enums.EstadoSimulacro;
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
    public List<Object[]> findTop10GlobalLibres(LocalDateTime fecha) {
        return springResultadoSimulacroRepository.findTop10GlobalLibres(fecha);
    }

    @Override
    public List<Object[]> findTop10ByArea(Long areaId) {
        return springResultadoSimulacroRepository.findTop10ByArea(areaId);
    }

    @Override
    public List<Double> findAllPuntajesLibresByArea(Long areaId) {
        return springResultadoSimulacroRepository.findAllPuntajesLibresByArea(areaId);
    }

    @Override
    public List<ResultadoSimulacro> findTop2ByUsuarioIdOrderByFechaEvaluacionDesc(Long usuarioId) {
        return springResultadoSimulacroRepository.findTop2ByUsuarioIdOrderByFechaEvaluacionDesc(usuarioId);
    }

    @Override
    public boolean existsByUsuarioIdAndEstado(Long usuarioId, EstadoSimulacro estado) {
        return springResultadoSimulacroRepository.existsByUsuarioIdAndEstado(usuarioId, estado);
    }

    @Override
    public List<ResultadoSimulacro> findByEstadoAndFechaExpiracionBefore(EstadoSimulacro estado, LocalDateTime fecha) {
        return springResultadoSimulacroRepository.findByEstadoAndFechaExpiracionBefore(estado, fecha);
    }

    @Override
    public List<Object[]> findRankingLibreByArea(Long areaId) {
        return springResultadoSimulacroRepository.findRankingLibreByArea(areaId);
    }

    @Override
    public Optional<ResultadoSimulacro> findByUsuarioIdAndEstado(Long usuarioId, EstadoSimulacro estado) {
        return springResultadoSimulacroRepository.findByUsuarioIdAndEstado(usuarioId, estado);
    }

    @Override
    public boolean existsByUsuarioIdAndSimulacroProgramadoId(Long usuarioId, Long eventoId) {
        return springResultadoSimulacroRepository.existsByUsuarioIdAndSimulacroProgramadoId(usuarioId, eventoId);
    }

    @Override
    public List<Object[]> findRankingOficialByEvento(Long eventoId) {
        return springResultadoSimulacroRepository.findRankingOficialByEvento(eventoId);
    }

    @Override
    public List<Object[]> findRankingGlobalCompletoByEvento(Long eventoId) {
        return springResultadoSimulacroRepository.findRankingGlobalCompletoByEvento(eventoId);
    }

    @Override
    public List<Object[]> findTop10GlobalByEvento(Long eventoId) {
        return springResultadoSimulacroRepository.findTop10GlobalByEvento(eventoId);
    }

    @Override
    public List<Object[]> findTop10ByAreaAndEvento(Long eventoId, Long areaId) {
        return springResultadoSimulacroRepository.findTop10ByAreaAndEvento(eventoId, areaId);
    }

    @Override
    public List<Object[]> findRankingCompletoByAreaAndEvento(Long eventoId, Long areaId) {
        return springResultadoSimulacroRepository.findRankingCompletoByAreaAndEvento(eventoId, areaId);
    }

    @Override
    public Object[] obtenerMetricasGlobalesDelEvento(Long eventoId) {
        return springResultadoSimulacroRepository.obtenerMetricasGlobalesDelEvento(eventoId);
    }
}
