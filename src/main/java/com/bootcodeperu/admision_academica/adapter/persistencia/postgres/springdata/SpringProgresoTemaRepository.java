package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcodeperu.admision_academica.domain.model.ProgresoTema;

public interface SpringProgresoTemaRepository extends JpaRepository<ProgresoTema, Long> {
    Optional<ProgresoTema> findByUsuarioIdAndTemaId(Long usuarioId, Long temaId);
    List<ProgresoTema> findAllByUsuarioId(Long usuarioId);
}