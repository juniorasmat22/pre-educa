package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringObjetivoAcademicoRepository;
import com.bootcodeperu.admision_academica.domain.model.ObjetivoAcademico;
import com.bootcodeperu.admision_academica.domain.repository.ObjetivoAcademicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ObjetivoAcademicoRepositoryImpl implements ObjetivoAcademicoRepository {

    private final SpringObjetivoAcademicoRepository springRepository;

    @Override
    public List<ObjetivoAcademico> findAll() {
        return springRepository.findAll();
    }

    @Override
    public Optional<ObjetivoAcademico> findById(Long id) {
        return springRepository.findById(id);
    }

    @Override
    public ObjetivoAcademico save(ObjetivoAcademico objetivo) {
        return springRepository.save(objetivo);
    }

    @Override
    public List<ObjetivoAcademico> saveAll(List<ObjetivoAcademico> objetivos) {
        return springRepository.saveAll(objetivos);
    }

    @Override
    public Optional<ObjetivoAcademico> findByUsuarioIdAndObjetivoPrincipalTrueAndActivoTrue(Long usuarioId) {
        return springRepository.findByUsuarioIdAndObjetivoPrincipalTrueAndActivoTrue(usuarioId);
    }

    @Override
    public List<ObjetivoAcademico> findByUsuarioIdAndActivoTrue(Long usuarioId) {
        return springRepository.findByUsuarioIdAndActivoTrue(usuarioId);
    }
}
