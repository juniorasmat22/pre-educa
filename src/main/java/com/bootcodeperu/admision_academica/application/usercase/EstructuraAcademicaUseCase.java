package com.bootcodeperu.admision_academica.application.usercase;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bootcodeperu.admision_academica.application.service.EstructuraAcademicaService;
import com.bootcodeperu.admision_academica.domain.model.Area;
import com.bootcodeperu.admision_academica.domain.model.Curso;
import com.bootcodeperu.admision_academica.domain.model.CursoArea;
import com.bootcodeperu.admision_academica.domain.model.Tema;
import com.bootcodeperu.admision_academica.domain.repository.AreaRepository;
import com.bootcodeperu.admision_academica.domain.repository.CursoAreaRepository;
import com.bootcodeperu.admision_academica.domain.repository.CursoRepository;
import com.bootcodeperu.admision_academica.domain.repository.TemaRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class EstructuraAcademicaUseCase implements EstructuraAcademicaService{
	// Inyección de todos los repositorios de estructura
    private final AreaRepository areaRepository;
    private final CursoRepository cursoRepository;
    private final CursoAreaRepository cursoAreaRepository;
    private final TemaRepository temaRepository;

    @Override
    public List<Area> getAllAreas() {
        return areaRepository.findAll();
    }
    
    @Override
    public Optional<Area> findAreaById(Long id) {
        return areaRepository.findById(id);
    }
    
    @Override
    public List<Curso> getAllCursos() {
        // Asumiendo que CursoRepository tiene un findAll()
        return cursoRepository.findAll(); 
    }
    
    @Override
    public List<CursoArea> getDistribucionByArea(Long areaId) {
        return cursoAreaRepository.findAllByAreaId(areaId);
    }
    
    @Override
    public Area saveArea(Area area) {
        // Lógica de validación si es necesaria
        return areaRepository.save(area);
    }
    
    @Override
    public Curso saveCurso(Curso curso) {
        return cursoRepository.save(curso);
    }
    
    @Override
    public CursoArea saveCursoArea(CursoArea cursoArea) {
        return cursoAreaRepository.save(cursoArea);
    }
    
    @Override
    public Tema saveTema(Tema tema) {
        return temaRepository.save(tema);
    }
}
