package com.bootcodeperu.admision_academica.application.usercase;


import com.bootcodeperu.admision_academica.adapter.mapper.PreguntaDetalleMapper;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.PreguntaDetalle;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.repository.PreguntaDetalleMongoRepository;
import com.bootcodeperu.admision_academica.application.controller.dto.analitica.RankingUsuarioResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaDetalleResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.simulacro.SimulacroProgramadoResponse;
import com.bootcodeperu.admision_academica.application.service.SimulacroProgramadoService;
import com.bootcodeperu.admision_academica.domain.exception.BusinessException;
import com.bootcodeperu.admision_academica.domain.exception.ContentLoadingException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.*;
import com.bootcodeperu.admision_academica.domain.model.enums.EstadoSimulacro;
import com.bootcodeperu.admision_academica.domain.model.enums.QuestionTarget;
import com.bootcodeperu.admision_academica.domain.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimulacroProgramadoUseCase implements SimulacroProgramadoService {

    private final SimulacroProgramadoRepository eventoRepository;
    private final AreaRepository areaRepository;
    private final CursoAreaRepository cursoAreaRepository;
    private final TemaRepository temaRepository;
    private final MetadatoPreguntaRepository metadatoPreguntaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ResultadoSimulacroRepository resultadoSimulacroRepository;
    private final PreguntaDetalleMongoRepository preguntaDetalleMongoRepository;
    private final PreguntaDetalleMapper preguntaDetalleMapper;

    @Override
    @Transactional
    public SimulacroProgramadoResponse crearSimulacroOficial(Long areaId, String titulo, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.info("Creando evento oficial '{}' para el areaId: {}", titulo, areaId);

        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new ResourceNotFoundException("Área", "id", areaId));

        if (fechaInicio.isAfter(fechaFin)) {
            throw new BusinessException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        // 1. Obtener IDs de preguntas del banco (Misma lógica que el simulacro libre)
        List<String> mongoIdsSeleccionados = generarIdsAleatoriosParaArea(areaId);

        // 2. Crear y guardar el Evento
        SimulacroProgramado evento = new SimulacroProgramado();
        evento.setTitulo(titulo);
        evento.setArea(area);
        evento.setFechaInicio(fechaInicio);
        evento.setFechaFin(fechaFin);
        evento.setDuracionMinutos(area.getDuracionMinutos());
        evento.setPreguntasIds(mongoIdsSeleccionados); // Guardamos la "Clave Maestra" de preguntas

        SimulacroProgramado eventoGuardado = eventoRepository.save(evento);

        return mapearAResponse(eventoGuardado);
    }

    @Override
    public List<SimulacroProgramadoResponse> listarEventosDisponibles(Long areaId) {
        // Traemos los eventos de esa área cuya fecha de fin aún no haya pasado
        LocalDateTime ahora = LocalDateTime.now();
        List<SimulacroProgramado> eventos = eventoRepository.findByAreaIdAndFechaFinAfter(areaId, ahora);

        return eventos.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PreguntaDetalleResponse> iniciarSimulacroOficial(Long usuarioId, Long eventoId) {
        log.info("Usuario {} iniciando evento oficial {}", usuarioId, eventoId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        SimulacroProgramado evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento Oficial", "id", eventoId));

        LocalDateTime ahora = LocalDateTime.now();

        // 1. Validar Ventana de Tiempo
        if (ahora.isBefore(evento.getFechaInicio())) {
            throw new BusinessException("El simulacro oficial aún no ha comenzado. Inicia el: " + evento.getFechaInicio());
        }
        if (ahora.isAfter(evento.getFechaFin())) {
            throw new BusinessException("El simulacro oficial ya ha finalizado.");
        }

        // 2. Control de Intentos: ¿Ya tiene una sesión (EN_CURSO, FINALIZADO o EXPIRADO) para este evento?
        if (resultadoSimulacroRepository.existsByUsuarioIdAndSimulacroProgramadoId(usuarioId, eventoId)) {
            throw new BusinessException("Ya has participado o tienes una sesión activa en este simulacro oficial.");
        }

        // 3. Crear Sesión EN_CURSO vinculada a este Evento
        ResultadoSimulacro sesion = new ResultadoSimulacro();
        sesion.setUsuario(usuario);
        sesion.setAreaEvaluada(evento.getArea());
        sesion.setSimulacroProgramado(evento); // VINCULACIÓN AL EVENTO OFICIAL
        sesion.setEstado(EstadoSimulacro.EN_CURSO);
        sesion.setFechaEvaluacion(ahora);
        sesion.setFechaExpiracion(ahora.plusMinutes(evento.getDuracionMinutos()));

        resultadoSimulacroRepository.save(sesion);

        // 4. Obtener las preguntas idénticas para todos desde MongoDB
        Map<String, PreguntaDetalle> preguntasMap = preguntaDetalleMongoRepository
                .findAllByIdIn(evento.getPreguntasIds()).stream()
                .collect(Collectors.toMap(PreguntaDetalle::getId, p -> p));

        // 5. Retornar respetando el orden original del evento
        return evento.getPreguntasIds().stream()
                .map(preguntasMap::get)
                .filter(Objects::nonNull)
                .map(preguntaDetalleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RankingUsuarioResponse> obtenerRankingPorEvento(Long eventoId) {
        // Delegamos al Query nativo con DENSE_RANK y desempate en la base de datos
        List<Object[]> resultados = resultadoSimulacroRepository.findRankingOficialByEvento(eventoId);

        List<RankingUsuarioResponse> ranking = new ArrayList<>();
        for (int i = 0; i < resultados.size(); i++) {
            Object[] row = resultados.get(i);
            ranking.add(new RankingUsuarioResponse(
                    i + 1, // Puesto
                    (String) row[0], // Nombre
                    (Double) row[1], // Puntaje Total
                    (String) row[2]  // Carrera (Área)
            ));
        }
        return ranking;
    }

    // =======================================================
    // MÉTODOS PRIVADOS DE APOYO
    // =======================================================

    private SimulacroProgramadoResponse mapearAResponse(SimulacroProgramado evento) {
        String estado;
        LocalDateTime ahora = LocalDateTime.now();
        if (ahora.isBefore(evento.getFechaInicio())) estado = "PROGRAMADO";
        else if (ahora.isAfter(evento.getFechaFin())) estado = "FINALIZADO";
        else estado = "EN_CURSO";

        return new SimulacroProgramadoResponse(
                evento.getId(),
                evento.getTitulo(),
                evento.getArea().getNombre(),
                evento.getFechaInicio(),
                evento.getFechaFin(),
                evento.getDuracionMinutos(),
                estado
        );
    }

    private List<String> generarIdsAleatoriosParaArea(Long areaId) {
        List<CursoArea> distribucion = cursoAreaRepository.findAllByAreaId(areaId);
        if (distribucion.isEmpty()) {
            throw new ContentLoadingException("No se encontró distribución de cursos para el área.");
        }

        List<String> mongoIdsSeleccionados = new ArrayList<>();
        for (CursoArea ca : distribucion) {
            Long cursoId = ca.getCurso().getId();
            int cantidadRequerida = ca.getCantidadPreguntas();

            List<Long> temaIds = temaRepository.findIdsByCursoId(cursoId);
            if (temaIds.isEmpty()) continue;

            List<MetadatoPregunta> metadatos = metadatoPreguntaRepository
                    .findRandomByTemaIdInAndTarget(temaIds, QuestionTarget.EXAM.name(), cantidadRequerida);

            mongoIdsSeleccionados.addAll(metadatos.stream().map(MetadatoPregunta::getMongoIdPregunta).toList());
        }

        if (mongoIdsSeleccionados.isEmpty()) {
            throw new ContentLoadingException("No se pudieron cargar preguntas del banco.");
        }
        return mongoIdsSeleccionados;
    }
}