package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringCursoAreaRepository;
import com.bootcodeperu.admision_academica.domain.model.CursoArea;
import com.bootcodeperu.admision_academica.domain.model.CursoAreaId;
import com.bootcodeperu.admision_academica.domain.repository.CursoAreaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CursoAreaRepositoryImpl implements CursoAreaRepository {

    private final SpringCursoAreaRepository springCursoAreaRepository;

    @Override
    public CursoArea save(CursoArea cursoArea) {
        return springCursoAreaRepository.save(cursoArea);
    }

    @Override
    public Optional<CursoArea> findById(CursoAreaId id) {
        return springCursoAreaRepository.findById(id);
    }

    @Override
    public List<CursoArea> findAllByAreaId(Long areaId) {
        return springCursoAreaRepository.findAllByAreaId(areaId);
    }
}
