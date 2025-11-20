package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringTemaRepository;
import com.bootcodeperu.admision_academica.domain.model.Tema;
import com.bootcodeperu.admision_academica.domain.repository.TemaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TemaRepositoryImpl implements TemaRepository {

    private final SpringTemaRepository springTemaRepository;

    @Override
    public Tema save(Tema tema) {
        return springTemaRepository.save(tema);
    }

    @Override
    public Optional<Tema> findById(Long id) {
        return springTemaRepository.findById(id);
    }

    @Override
    public List<Tema> findAllByCursoId(Long cursoId) {
        return springTemaRepository.findAllByCursoId(cursoId);
    }

    @Override
    public List<Long> findIdsByCursoId(Long cursoId) {
        // Usa el método con la consulta @Query definida en SpringTemaRepository
        return springTemaRepository.findIdsByCursoId(cursoId);
    }
}