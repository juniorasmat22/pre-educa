package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bootcodeperu.admision_academica.domain.model.Token;

public interface SpringTokenRepository extends JpaRepository<Token, Long> {
    // Consulta clave para validar un token: buscar un token que no esté expirado ni revocado
    @Query(value = "SELECT t FROM Token t INNER JOIN Usuario u ON t.usuario.id = u.id " +
                   "WHERE u.id = :id AND (t.revocado = false AND t.expirado = false)")
    List<Token> findAllValidTokenByUser(@Param("id") Long id);

    Optional<Token> findByToken(String token);
}