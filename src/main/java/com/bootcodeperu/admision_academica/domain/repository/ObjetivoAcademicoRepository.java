package com.bootcodeperu.admision_academica.domain.repository;

import com.bootcodeperu.admision_academica.domain.model.ObjetivoAcademico;

import java.util.List;
import java.util.Optional;

public interface ObjetivoAcademicoRepository {
    List<ObjetivoAcademico> findAll();

    Optional<ObjetivoAcademico> findById(Long id);

    ObjetivoAcademico save(ObjetivoAcademico objetivo);

    List<ObjetivoAcademico> saveAll(List<ObjetivoAcademico> objetivos);

    // Busca el objetivo principal y activo de un usuario (para saber en qué área está)
    Optional<ObjetivoAcademico> findByUsuarioIdAndObjetivoPrincipalTrueAndActivoTrue(Long usuarioId);

    // Busca todos los objetivos activos (normalmente debería ser solo 1, pero sirve para el historial)
    List<ObjetivoAcademico> findByUsuarioIdAndActivoTrue(Long usuarioId);
}