package com.bootcodeperu.admision_academica.application.service;

import java.util.List;

import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CursoAreaResponse;
import com.bootcodeperu.admision_academica.domain.model.CursoArea;

public interface EstructuraAcademicaService {
    List<CursoAreaResponse> getDistribucionByArea(Long areaId);
    CursoAreaResponse saveCursoArea(CursoArea cursoArea);
}
