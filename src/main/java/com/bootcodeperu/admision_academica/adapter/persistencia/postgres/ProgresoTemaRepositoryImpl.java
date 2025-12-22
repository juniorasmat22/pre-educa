package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringProgresoTemaRepository;
import com.bootcodeperu.admision_academica.domain.model.ProgresoTema;
import com.bootcodeperu.admision_academica.domain.repository.ProgresoTemaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProgresoTemaRepositoryImpl implements ProgresoTemaRepository {

    private final SpringProgresoTemaRepository springProgresoTemaRepository;

    @Override
    public ProgresoTema save(ProgresoTema progresoTema) {
        return springProgresoTemaRepository.save(progresoTema);
    }

    @Override
    public Optional<ProgresoTema> findByUsuarioIdAndTemaId(Long usuarioId, Long temaId) {
        return springProgresoTemaRepository.findByUsuarioIdAndTemaId(usuarioId, temaId);
    }

    @Override
    public List<ProgresoTema> findAllByUsuarioId(Long usuarioId) {
        return springProgresoTemaRepository.findAllByUsuarioId(usuarioId);
    }

    @Override
    public List<ProgresoTema> findByUsuarioIdAndPuntajePromedioLessThan(Long usuarioId, Double umbral) {
        return springProgresoTemaRepository.findByUsuarioIdAndPuntajePromedioLessThan(usuarioId, umbral);
    }
}