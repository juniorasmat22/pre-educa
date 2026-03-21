package com.bootcodeperu.admision_academica.domain.repository;

import com.bootcodeperu.admision_academica.domain.model.ProcesoAdmision;

import java.util.List;
import java.util.Optional;

public interface ProcesoAdmisionRepository {
    ProcesoAdmision save(ProcesoAdmision proceso);

    Optional<ProcesoAdmision> findById(Long id);

    List<ProcesoAdmision> findAll();

    List<ProcesoAdmision> findByVigenteTrue();

    List<ProcesoAdmision> findByUniversidadIdAndVigenteTrue(Long idUniversidad);
}
