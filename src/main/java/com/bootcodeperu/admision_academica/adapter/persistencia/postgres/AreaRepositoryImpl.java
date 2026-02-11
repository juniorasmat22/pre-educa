package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Area> findAll(String search, Pageable pageable) {
        return springAreaRepository.findAllWithSearch(search, pageable);
    }

    @Override
    public Page<Area> findAllActive(String search, Pageable pageable) {
        return springAreaRepository.findAllActiveWithSearch(search, pageable);
    }

    @Override
    public Optional<Area> findByName(String name) {
        return springAreaRepository.findByNombre(name);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return springAreaRepository.existsByNombre(nombre);
    }

    @Override
    public boolean existsByDescripcion(String descripcion) {
        return springAreaRepository.existsByDescripcion(descripcion);
    }

    @Override
    public boolean existsByNombreAndIdNot(String nombre, Long id) {
        return springAreaRepository.existsByNombreAndIdNot(nombre, id);
    }
}