package com.bootcodeperu.admision_academica.domain.repository;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.PreguntaDetalle;

import java.util.List;
import java.util.Optional;

public interface PreguntaRepository {
    PreguntaDetalle save(PreguntaDetalle pregunta);

    Optional<PreguntaDetalle> findById(String id);

    List<PreguntaDetalle> findAllByTemaId(Long temaId);

    List<PreguntaDetalle> findAllByIdIn(List<String> mongoIds);

    void delete(String id);
}
