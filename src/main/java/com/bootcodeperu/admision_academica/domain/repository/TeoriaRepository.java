package com.bootcodeperu.admision_academica.domain.repository;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.ContenidoTeoria;

import java.util.List;
import java.util.Optional;

public interface TeoriaRepository {
    ContenidoTeoria save(ContenidoTeoria teoria);

    List<ContenidoTeoria> findByTemaId(Long temaId);

    Optional<ContenidoTeoria> findById(String id);

    void delete(ContenidoTeoria teoria);

    List<ContenidoTeoria> findByTemaIdOrdered(Long idTemaSQL);
}
