package com.bootcodeperu.admision_academica.application.usercase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.bootcodeperu.admision_academica.adapter.mapper.ContenidoTeoriaMapper;
import com.bootcodeperu.admision_academica.adapter.mapper.PreguntaDetalleMapper;
import com.bootcodeperu.admision_academica.adapter.mapper.ProgresoTemaMapper;
import com.bootcodeperu.admision_academica.adapter.mapper.TemaMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaPracticaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.progresotema.ProgresoTemaResponse;
import com.bootcodeperu.admision_academica.domain.model.enums.QuestionTarget;
import com.bootcodeperu.admision_academica.domain.repository.*;
import org.springframework.stereotype.Service;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.ContenidoTeoria;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.PreguntaDetalle;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.tema.TemaResponse;
import com.bootcodeperu.admision_academica.application.service.ContenidoService;
import com.bootcodeperu.admision_academica.domain.exception.ContentLoadingException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.MetadatoPregunta;
import com.bootcodeperu.admision_academica.domain.model.ProgresoTema;
import com.bootcodeperu.admision_academica.domain.model.Tema;
import com.bootcodeperu.admision_academica.domain.model.Usuario;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContenidoUseCase implements ContenidoService {
    private final TemaRepository temaRepository;
    private final ProgresoTemaRepository progresoTemaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MetadatoPreguntaRepository metadatoPreguntaRepository;
    private final PreguntaRepository preguntaRepository;
    private final TeoriaRepository TeoriaRepository;
    private final TemaMapper temaMapper;
    private final PreguntaDetalleMapper preguntaDetalleMapper;
    private final ContenidoTeoriaMapper contenidoTeoriaMapper;
    private final ProgresoTemaMapper progresoTemaMapper;

    @Override
    public List<TemaResponse> getTemasByCursoId(Long cursoId) {
        return temaRepository.findAllByCursoId(cursoId).stream().map(temaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PreguntaPracticaResponse> getPreguntasPractica(Long temaId, String nivel) {

        // 1. Obtener Metadatos (PostgreSQL)
        List<MetadatoPregunta> metadatos = metadatoPreguntaRepository.findByTemaIdAndNivelAndTarget(temaId, nivel, QuestionTarget.PRACTICE);

        if (metadatos.isEmpty()) {
            throw new ContentLoadingException(
                    String.format("No hay preguntas de práctica para el Tema %d en el nivel %s.", temaId, nivel));
        }

        // 2. Obtener IDs de Mongo
        List<String> mongoIds = metadatos.stream().map(MetadatoPregunta::getMongoIdPregunta)
                .collect(Collectors.toList());

        // 3. Obtener Detalles (MongoDB)
        List<PreguntaDetalle> preguntasDetalle = preguntaRepository.findAllByIdIn(mongoIds);

        if (preguntasDetalle.size() != mongoIds.size()) {
            // Esto indica un problema de sincronización entre las BD
            throw new ContentLoadingException("Error de sincronización: Falta detalle de algunas preguntas.");
        }

        return preguntasDetalle.stream().map(preguntaDetalleMapper::toPractica)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProgresoTemaResponse completarTeoria(Long usuarioId, Long temaId) {
        // Asegurarse de que el usuario y el tema existan
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        Tema tema = temaRepository.findById(temaId)
                .orElseThrow(() -> new ResourceNotFoundException("Tema", "id", temaId));

        // Buscar el progreso existente o crear uno nuevo
        ProgresoTema progreso = progresoTemaRepository.findByUsuarioIdAndTemaId(usuarioId, temaId).orElseGet(() -> {
            // Si no existe, crea un nuevo registro de progreso
            ProgresoTema nuevoProgreso = new ProgresoTema();
            nuevoProgreso.setUsuario(usuario);
            nuevoProgreso.setTema(tema);
            return nuevoProgreso;
        });
        // Actualizar el estado de la teoría
        if (!Boolean.TRUE.equals(progreso.getTeoriaCompletada())) {
            progreso.setTeoriaCompletada(true);
        }
        return progresoTemaMapper.toResponse(progresoTemaRepository.save(progreso));
    }

    @Override
    public List<ContenidoTeoriaResponse> getContenidoTeoriaByTemaId(Long temaId) {
        // No verificamos el Tema aquí, delegamos la responsabilidad a Mongo o manejamos
        // la lista vacía.
        List<ContenidoTeoria> teoriaList = TeoriaRepository.findByTemaIdOrdered(temaId);

        // Mapeo de Documento Mongo a DTO de Respuesta
        return teoriaList.stream().map(contenidoTeoriaMapper::toResponse)
                .collect(Collectors.toList());
    }
}
