package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import com.bootcodeperu.admision_academica.domain.model.ProcesoAdmision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringProcesoAdmisionRepository extends JpaRepository<ProcesoAdmision, Long> {

    List<ProcesoAdmision> findByVigenteTrue();

    List<ProcesoAdmision> findByUniversidadIdAndVigenteTrue(Long idUniversidad);
}