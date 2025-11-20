package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcodeperu.admision_academica.domain.model.Permiso;

public interface SpringPermisoRepository extends JpaRepository<Permiso, Long> {
}