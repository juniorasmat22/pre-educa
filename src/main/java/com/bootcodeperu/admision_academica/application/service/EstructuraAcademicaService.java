package com.bootcodeperu.admision_academica.application.service;

import java.util.List;

import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso.CursoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.curso.CursoResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CursoAreaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.tema.TemaResponse;
import com.bootcodeperu.admision_academica.domain.model.CursoArea;
import com.bootcodeperu.admision_academica.domain.model.Tema;

public interface EstructuraAcademicaService {
    List<AreaResponse> getAllAreas();
    AreaResponse findAreaById(Long id);

    List<CursoResponse> getAllCursos();
    List<CursoAreaResponse> getDistribucionByArea(Long areaId);
    
    // Métodos de administración (CRUD simple para el backend)
    AreaResponse saveArea(AreaRequest area);
    CursoResponse saveCurso(CursoRequest curso);
    CursoAreaResponse saveCursoArea(CursoArea cursoArea);
    TemaResponse saveTema(Tema tema);
}
