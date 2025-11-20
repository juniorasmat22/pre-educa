package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcodeperu.admision_academica.domain.model.Usuario;

public interface SpringUsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}