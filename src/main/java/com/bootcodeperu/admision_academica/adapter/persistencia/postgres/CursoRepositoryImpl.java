package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import org.springframework.stereotype.Component;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringCursoRepository;
import com.bootcodeperu.admision_academica.domain.model.Curso;
import com.bootcodeperu.admision_academica.domain.repository.CursoRepository;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CursoRepositoryImpl implements CursoRepository {

    private final SpringCursoRepository springCursoRepository;

    @Override
    public Curso save(Curso curso) {
        return springCursoRepository.save(curso);
    }

    @Override
    public Optional<Curso> findById(Long id) {
        return springCursoRepository.findById(id);
    }

    @Override
    public List<Curso> findAll() {
        return springCursoRepository.findAll();
    }

    @Override
    public Optional<Curso> findByNombre(String nombre) {
        return springCursoRepository.findByNombre(nombre);
    }
}