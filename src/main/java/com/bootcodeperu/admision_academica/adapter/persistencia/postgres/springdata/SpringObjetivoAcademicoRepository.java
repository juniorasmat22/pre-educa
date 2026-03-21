package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import com.bootcodeperu.admision_academica.domain.model.ObjetivoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringObjetivoAcademicoRepository extends JpaRepository<ObjetivoAcademico, Long> {

    Optional<ObjetivoAcademico> findByUsuarioIdAndObjetivoPrincipalTrueAndActivoTrue(Long usuarioId);

    List<ObjetivoAcademico> findByUsuarioIdAndActivoTrue(Long usuarioId);
}