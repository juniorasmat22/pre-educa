package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringPermisoRepository;
import com.bootcodeperu.admision_academica.domain.model.Permiso;
import com.bootcodeperu.admision_academica.domain.repository.PermisoRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PermisoRepositoryImpl implements PermisoRepository {

    private final SpringPermisoRepository springPermisoRepository;

    @Override
    public Permiso save(Permiso permiso) {
        return springPermisoRepository.save(permiso);
    }

    @Override
    public Optional<Permiso> findById(Long id) {
        return springPermisoRepository.findById(id);
    }

    @Override
    public List<Permiso> findAll() {
        return springPermisoRepository.findAll();
    }
}