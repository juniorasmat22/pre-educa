package com.bootcodeperu.admision_academica.domain.repository;

import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.ProgresoTema;

public interface ProgresoTemaRepository {
	ProgresoTema save(ProgresoTema progresoTema);
    // Método de negocio clave: Obtener el progreso de un usuario en un tema específico.
    Optional<ProgresoTema> findByUsuarioIdAndTemaId(Long usuarioId, Long temaId);
    // Obtener todo el progreso de un usuario.
    List<ProgresoTema> findAllByUsuarioId(Long usuarioId);
    // Nueva consulta para analítica:
    List<ProgresoTema> findByUsuarioIdAndPuntajePromedioLessThan(Long usuarioId, Double umbral);
}
