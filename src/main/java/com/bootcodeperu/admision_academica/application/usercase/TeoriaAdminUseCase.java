package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.mapper.ContenidoTeoriaMapper;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.ContenidoTeoria;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.repository.ContenidoTeoriaMongoRepository;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaResponse;
import com.bootcodeperu.admision_academica.application.service.TeoriaAdminService;
import com.bootcodeperu.admision_academica.domain.repository.TemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TeoriaAdminUseCase implements TeoriaAdminService {
    private final ContenidoTeoriaMongoRepository theoryRepository; // Puerto de Dominio
    private final TemaRepository temaRepository;
    private final ContenidoTeoriaMapper theoryMapper;

    @Override
    public ContenidoTeoriaResponse save(ContenidoTeoriaRequest request) {
        temaRepository.findById(request.idTemaSQL()).orElseThrow();
        ContenidoTeoria doc = theoryMapper.toDocument(request);
        return theoryMapper.toResponse(theoryRepository.save(doc));
    }

    @Override
    public ContenidoTeoriaResponse update(String id, ContenidoTeoriaRequest request) {
        ContenidoTeoria existing = theoryRepository.findById(id).orElseThrow();
        existing.setTitulo(request.titulo());
        existing.setSecciones(theoryMapper.toResponseList(request.secciones()));
        existing.setUltimaActualizacion(LocalDateTime.now());
        return theoryMapper.toResponse(theoryRepository.save(existing));
    }

    @Override
    public void delete(String id) {
        ContenidoTeoria existing = theoryRepository.findById(id).orElseThrow();
        theoryRepository.delete(existing);
    }
}
