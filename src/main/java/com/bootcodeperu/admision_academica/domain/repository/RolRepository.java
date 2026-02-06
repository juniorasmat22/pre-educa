package com.bootcodeperu.admision_academica.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.bootcodeperu.admision_academica.domain.model.Permiso;
import com.bootcodeperu.admision_academica.domain.model.Rol;

public interface RolRepository {
    Rol save(Rol rol);
    Optional<Rol> findById(Long id);
    Optional<Rol> findByNombre(String nombre);
    List<Rol> findAll();
    Boolean existsByNombre(String nombre);
    Set<Rol> findAllByPermisosContaining(Permiso permiso);
}