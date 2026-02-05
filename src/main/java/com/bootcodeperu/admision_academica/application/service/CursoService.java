package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.curso.CursoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.curso.CursoResponse;

import java.util.List;

public interface CursoService {
    List<CursoResponse> getAllCursos();
    CursoResponse findCursoById(Long id);
    CursoResponse createCurso(CursoRequest request);
    CursoResponse updateCurso(Long id, CursoRequest request);
}