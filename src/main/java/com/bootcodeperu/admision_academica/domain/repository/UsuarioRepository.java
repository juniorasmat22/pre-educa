package com.bootcodeperu.admision_academica.domain.repository;

import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import com.bootcodeperu.admision_academica.domain.model.Usuario;

public interface UsuarioRepository {
	Usuario save(Usuario usuario);
    Optional<Usuario> findById(Long id);
    List<Usuario> findAll();
    // Método de negocio clave: Usado para iniciar sesión y evitar duplicados.
    Optional<Usuario> findByEmail(String email);
}
