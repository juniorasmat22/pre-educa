package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.mapper.TemaMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.tema.TemaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.tema.TemaResponseDetalle;
import com.bootcodeperu.admision_academica.application.service.TemaService;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.Curso;
import com.bootcodeperu.admision_academica.domain.model.Tema;
import com.bootcodeperu.admision_academica.domain.repository.CursoRepository;
import com.bootcodeperu.admision_academica.domain.repository.TemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemaUseCase implements TemaService {
    private final TemaRepository temaRepository;
    private final CursoRepository cursoRepository;
    private final TemaMapper temaMapper;
    @Override
    @Transactional(readOnly = true)
    public List<TemaResponseDetalle> getAllTemas() {
        return temaRepository.findAll().stream()
                .map(temaMapper::toResponseDetalle)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TemaResponseDetalle> getAllTemasByCursoId(Long cursoId) {
        if (!cursoRepository.existsById(cursoId)) {
            throw new ResourceNotFoundException("Curso not found with ID: " + cursoId);
        }
        return temaRepository.findAllByCursoId(cursoId).stream()
                .map(temaMapper::toResponseDetalle)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TemaResponseDetalle findTemaById(Long id) {
        Tema tema = temaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tema not found with ID: " + id));
        return temaMapper.toResponseDetalle(tema);
    }

    @Override
    @Transactional
    public TemaResponseDetalle createTema(TemaRequest request) {
        // 1. Buscar el curso padre
        Curso curso = cursoRepository.findById(request.cursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso not found with ID: " + request.cursoId()));

        // 2. Crear la entidad
        Tema tema = new Tema();
        tema.setNombreTema(request.nombreTema());
        tema.setCurso(curso);

        // 3. Guardar y retornar DTO
        return temaMapper.toResponseDetalle(temaRepository.save(tema));
    }

    @Override
    @Transactional
    public TemaResponseDetalle updateTema(Long id, TemaRequest request) {
        Tema tema = temaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tema not found with ID: " + id));
        // Si se quiere cambiar el tema de curso
        if (!tema.getCurso().getId().equals(request.cursoId())) {
            Curso nuevoCurso = cursoRepository.findById(request.cursoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso not found with ID: " + request.cursoId()));
            tema.setCurso(nuevoCurso);
        }
        tema.setNombreTema(request.nombreTema());
        return temaMapper.toResponseDetalle(temaRepository.save(tema));
    }
}
