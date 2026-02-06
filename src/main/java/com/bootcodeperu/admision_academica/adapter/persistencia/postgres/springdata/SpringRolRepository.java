package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import java.util.Optional;
import java.util.Set;

import com.bootcodeperu.admision_academica.domain.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcodeperu.admision_academica.domain.model.Rol;

public interface SpringRolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    Set<Rol> findAllByPermisosContaining(Permiso permiso);
}
