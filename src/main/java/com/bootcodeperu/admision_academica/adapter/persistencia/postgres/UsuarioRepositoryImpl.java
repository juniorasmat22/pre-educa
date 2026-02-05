package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.application.controller.dto.usuario.UsuarioResponse;
import org.springframework.stereotype.Component;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringUsuarioRepository;
import com.bootcodeperu.admision_academica.domain.model.Usuario;
import com.bootcodeperu.admision_academica.domain.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final SpringUsuarioRepository springUsuarioRepository;

    @Override
    public Usuario save(Usuario usuario) {
        return springUsuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return springUsuarioRepository.findById(id);
    }

    @Override
    public List<Usuario> findAll() {
        return springUsuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return springUsuarioRepository.findByEmail(email);
    }

}