package com.bootcodeperu.admision_academica.adapter.persistencia.mongo;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.PreguntaDetalle;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.repository.PreguntaDetalleMongoRepository;
import com.bootcodeperu.admision_academica.domain.repository.PreguntaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository   // También puede ser @Component
@RequiredArgsConstructor
public class PreguntaRepositoryImpl implements PreguntaRepository {
    private final PreguntaDetalleMongoRepository repository;

    @Override
    public PreguntaDetalle save(PreguntaDetalle pregunta) {
        return repository.save(pregunta);
    }

    @Override
    public Optional<PreguntaDetalle> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<PreguntaDetalle> findAllByTemaId(Long temaId) {
        return repository.findByIdTemaSQL(temaId);
    }

    @Override
    public List<PreguntaDetalle> findAllByIdIn(List<String> mongoIds) {
        return repository.findAllByIdIn(mongoIds);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}
