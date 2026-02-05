package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.mapper.CursoMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.curso.CursoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.curso.CursoResponse;
import com.bootcodeperu.admision_academica.application.service.CursoService;
import com.bootcodeperu.admision_academica.domain.exception.DuplicateResourceException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.Curso;
import com.bootcodeperu.admision_academica.domain.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CursoUseCase implements CursoService {
    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponse> getAllCursos() {
        return cursoRepository.findAll().stream().map(cursoMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponse findCursoById(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con ID: " + id));
        return cursoMapper.toResponse(curso);
    }

    @Override
    @Transactional
    public CursoResponse createCurso(CursoRequest request) {
        if (cursoRepository.existsByNombre(request.nombre())) {
            throw new DuplicateResourceException("El nombre del curso ya existe");
        }
        Curso curso = cursoMapper.toEntity(request);
        return cursoMapper.toResponse(cursoRepository.save(curso));
    }
    @Override
    @Transactional
    public CursoResponse updateCurso(Long id, CursoRequest request) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con ID:: " + id));
        if (cursoRepository.existsByNombre(request.nombre())
                && !curso.getNombre().equals(request.nombre())) {
            throw new DuplicateResourceException("El nombre del curso ya existe");
        }
        curso.setNombre(request.nombre());
        curso.setDescripcion(request.descripcion());
        return cursoMapper.toResponse(cursoRepository.save(curso));
    }
}
