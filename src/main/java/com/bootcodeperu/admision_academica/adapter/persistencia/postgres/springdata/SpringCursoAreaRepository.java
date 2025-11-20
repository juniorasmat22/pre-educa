package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcodeperu.admision_academica.domain.model.CursoArea;
import com.bootcodeperu.admision_academica.domain.model.CursoAreaId;

public interface SpringCursoAreaRepository extends JpaRepository<CursoArea, CursoAreaId> {
    List<CursoArea> findAllByAreaId(Long areaId);
}