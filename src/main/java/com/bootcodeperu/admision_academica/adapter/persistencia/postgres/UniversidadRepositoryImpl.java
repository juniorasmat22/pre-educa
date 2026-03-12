package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringUniversidadRepository;
import com.bootcodeperu.admision_academica.domain.model.Universidad;
import com.bootcodeperu.admision_academica.domain.repository.UniversidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UniversidadRepositoryImpl implements UniversidadRepository {

    private final SpringUniversidadRepository springRepository;

    @Override
    public Universidad save(Universidad universidad) {
        return springRepository.save(universidad);
    }

    @Override
    public Optional<Universidad> findById(Long id) {
        return springRepository.findById(id);
    }

    @Override
    public List<Universidad> findAll() {
        return springRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        springRepository.deleteById(id);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return springRepository.existsByNombreIgnoreCase(nombre);
    }

    @Override
    public List<Universidad> obtenerEstructuraActiva() {
        return springRepository.findAllEstructuraActiva();
    }
}