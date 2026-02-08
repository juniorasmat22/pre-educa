package com.bootcodeperu.admision_academica.adapter.persistencia.mongo;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.ContenidoTeoria;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.repository.ContenidoTeoriaMongoRepository;
import com.bootcodeperu.admision_academica.domain.repository.TeoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TeoriaRepositoryImpl implements TeoriaRepository {
    private final ContenidoTeoriaMongoRepository repository;

    @Override
    public ContenidoTeoria save(ContenidoTeoria teoria) {
        return repository.save(teoria);
    }

    @Override
    public List<ContenidoTeoria> findByTemaId(Long temaId) {
        return repository.findByTemaIdOrdered(temaId);
    }

    @Override
    public Optional<ContenidoTeoria> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public void delete(ContenidoTeoria teoria) {
        repository.delete(teoria);
    }

    @Override
    public List<ContenidoTeoria> findByTemaIdOrdered(Long idTemaSQL) {
        return repository.findByTemaIdOrdered(idTemaSQL);
    }
}
