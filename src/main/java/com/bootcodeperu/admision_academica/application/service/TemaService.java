package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.tema.TemaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.tema.TemaResponseDetalle;

import java.util.List;

public interface TemaService {
    List<TemaResponseDetalle> getAllTemas();

    List<TemaResponseDetalle> getAllTemasByCursoId(Long cursoId);

    TemaResponseDetalle findTemaById(Long id);

    TemaResponseDetalle createTema(TemaRequest request);

    TemaResponseDetalle updateTema(Long id, TemaRequest request);

}
