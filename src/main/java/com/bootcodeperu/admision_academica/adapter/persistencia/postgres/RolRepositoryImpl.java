package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringRolRepository;
import com.bootcodeperu.admision_academica.domain.model.Rol;
import com.bootcodeperu.admision_academica.domain.repository.RolRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RolRepositoryImpl implements RolRepository {

    private final SpringRolRepository springRolRepository;

    @Override
    public Rol save(Rol rol) {
        return springRolRepository.save(rol);
    }

    @Override
    public Optional<Rol> findById(Long id) {
        return springRolRepository.findById(id);
    }
    
    @Override
    public Optional<Rol> findByNombre(String nombre) {
        return springRolRepository.findByNombre(nombre);
    }

    @Override
    public List<Rol> findAll() {
        return springRolRepository.findAll();
    }

    @Override
    public Boolean existsByNombre(String nombre) {
        return springRolRepository.existsByNombre(nombre);
    }
}