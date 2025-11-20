package com.bootcodeperu.admision_academica.domain.repository;

import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.Permiso;

public interface PermisoRepository {
    Permiso save(Permiso permiso);
    Optional<Permiso> findById(Long id);
    List<Permiso> findAll();
}