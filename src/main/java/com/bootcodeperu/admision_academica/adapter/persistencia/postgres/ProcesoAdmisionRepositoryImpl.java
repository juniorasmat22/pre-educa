package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringProcesoAdmisionRepository;
import com.bootcodeperu.admision_academica.domain.model.ProcesoAdmision;
import com.bootcodeperu.admision_academica.domain.repository.ProcesoAdmisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProcesoAdmisionRepositoryImpl implements ProcesoAdmisionRepository {

    private final SpringProcesoAdmisionRepository springRepository;

    @Override
    public ProcesoAdmision save(ProcesoAdmision proceso) {
        return springRepository.save(proceso);
    }

    @Override
    public Optional<ProcesoAdmision> findById(Long id) {
        return springRepository.findById(id);
    }

    @Override
    public List<ProcesoAdmision> findAll() {
        return springRepository.findAll();
    }

    @Override
    public List<ProcesoAdmision> findByVigenteTrue() {
        return springRepository.findByVigenteTrue();
    }

    @Override
    public List<ProcesoAdmision> findByUniversidadIdAndVigenteTrue(Long idUniversidad) {
        return springRepository.findByUniversidadIdAndVigenteTrue(idUniversidad);
    }
}