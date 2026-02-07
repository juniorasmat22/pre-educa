package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringCarreraRepository;
import com.bootcodeperu.admision_academica.domain.model.Carrera;
import com.bootcodeperu.admision_academica.domain.repository.CarreraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
@RequiredArgsConstructor
public class CarreraRepositoryImpl implements CarreraRepository {
    private final SpringCarreraRepository springCarreraRepository;
    @Override
    public Carrera save(Carrera carrera) {
        return springCarreraRepository.save(carrera);
    }

    @Override
    public Optional<Carrera> findById(Long id) {
        return springCarreraRepository.findById(id);
    }

    @Override
    public List<Carrera> findAll() {
        return springCarreraRepository.findAll();
    }

    @Override
    public List<Carrera> findByAreaId(Long areaId) {
        return springCarreraRepository.findByAreaId(areaId);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return springCarreraRepository.existsByNombre(nombre);
    }
}
