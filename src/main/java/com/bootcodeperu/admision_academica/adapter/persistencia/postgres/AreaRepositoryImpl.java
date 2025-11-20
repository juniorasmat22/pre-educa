package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringAreaRepository;
import com.bootcodeperu.admision_academica.domain.model.Area;
import com.bootcodeperu.admision_academica.domain.repository.AreaRepository;

import lombok.RequiredArgsConstructor;

@Repository   // También puede ser @Component
@RequiredArgsConstructor
public class AreaRepositoryImpl implements AreaRepository {

    private final SpringAreaRepository springAreaRepository;

    @Override
    public Area save(Area area) {
        return springAreaRepository.save(area);
    }

    @Override
    public Optional<Area> findById(Long id) {
        return springAreaRepository.findById(id);
    }

    @Override
    public List<Area> findAll() {
        return springAreaRepository.findAll();
    }

    @Override
    public Optional<Area> findByName(String name) {
        return springAreaRepository.findByNombre(name);
    }
}