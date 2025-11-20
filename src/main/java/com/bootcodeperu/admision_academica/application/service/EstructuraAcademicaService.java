package com.bootcodeperu.admision_academica.application.service;

import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.Area;
import com.bootcodeperu.admision_academica.domain.model.Curso;
import com.bootcodeperu.admision_academica.domain.model.CursoArea;
import com.bootcodeperu.admision_academica.domain.model.Tema;

public interface EstructuraAcademicaService {
    List<Area> getAllAreas();
    Optional<Area> findAreaById(Long id);
    
    List<Curso> getAllCursos();
    List<CursoArea> getDistribucionByArea(Long areaId);
    
    // Métodos de administración (CRUD simple para el backend)
    Area saveArea(Area area);
    Curso saveCurso(Curso curso);
    CursoArea saveCursoArea(CursoArea cursoArea);
    Tema saveTema(Tema tema);
}
