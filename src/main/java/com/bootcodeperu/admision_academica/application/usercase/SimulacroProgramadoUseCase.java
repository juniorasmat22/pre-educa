package com.bootcodeperu.admision_academica.application.usercase;


import com.bootcodeperu.admision_academica.adapter.mapper.PreguntaDetalleMapper;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.PreguntaDetalle;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.repository.PreguntaDetalleMongoRepository;
import com.bootcodeperu.admision_academica.application.controller.dto.analitica.EstadisticasEventoResponse;
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
        List<Object[]> resultados = resultadoSimulacroRepository.findRankingOficialByEvento(eventoId);

        return mapearARanking(resultados);
    }

    @Override
    public List<RankingUsuarioResponse> obtenerTop10Global(Long eventoId) {
        List<Object[]> resultados = resultadoSimulacroRepository.findTop10GlobalByEvento(eventoId);
        return mapearARanking(resultados);
    }

    @Override
    public List<RankingUsuarioResponse> obtenerRankingGlobalCompleto(Long eventoId) {
        List<Object[]> resultados = resultadoSimulacroRepository.findRankingGlobalCompletoByEvento(eventoId);
        return mapearARanking(resultados);
    }

    @Override
    public List<RankingUsuarioResponse> obtenerTop10PorArea(Long eventoId, Long areaId) {
        List<Object[]> resultados = resultadoSimulacroRepository.findTop10ByAreaAndEvento(eventoId, areaId);
        return mapearARanking(resultados);
    }

    @Override
    public List<RankingUsuarioResponse> obtenerRankingCompletoPorArea(Long eventoId, Long areaId) {
        List<Object[]> resultados = resultadoSimulacroRepository.findRankingCompletoByAreaAndEvento(eventoId, areaId);
        return mapearARanking(resultados);//findRankingCompletoByAreaAndEvento
    }

    @Override
    public EstadisticasEventoResponse obtenerEstadisticasEvento(Long eventoId) {
        // 1. Validamos que el evento exista para obtener su título
        SimulacroProgramado evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento Oficial", "id", eventoId));

        // 2. Ejecutamos la consulta super-optimizada
        Object[] row = resultadoSimulacroRepository.obtenerMetricasGlobalesDelEvento(eventoId);

        // Si no hay datos (row viene vacío), devolvemos un reporte en ceros
        if (row == null || row.length == 0 || row[0] == null) {
            // Objeto vacío si aún nadie se inscribe
            return new EstadisticasEventoResponse(eventoId, evento.getTitulo(), 0, 0, 0, 0.0, 0.0, 0.0, 0.0);
        }

        // El repositorio devuelve una sola fila, pero Spring Data nativo con Object[] múltiple devuelve un Object[][]
        // Como no usamos GROUP BY, sabemos que es solo 1 fila con 7 columnas.
        Object[] metricas = (Object[]) row[0];

        // 3. Mapeo seguro de los resultados (Casteo de tipos devueltos por Postgres)
        Integer inscritos = metricas[0] != null ? ((Number) metricas[0]).intValue() : 0;
        Integer finalizados = metricas[1] != null ? ((Number) metricas[1]).intValue() : 0;
        Integer abandonos = metricas[2] != null ? ((Number) metricas[2]).intValue() : 0;

        Double promPuntaje = metricas[3] != null ? ((Number) metricas[3]).doubleValue() : 0.0;
        Double maxPuntaje = metricas[4] != null ? ((Number) metricas[4]).doubleValue() : 0.0;
        Double minPuntaje = metricas[5] != null ? ((Number) metricas[5]).doubleValue() : 0.0;
        Double promTiempo = metricas[6] != null ? ((Number) metricas[6]).doubleValue() : 0.0;

        // Redondeamos el promedio a 2 decimales para que se vea limpio en el Frontend
        promPuntaje = Math.round(promPuntaje * 100.0) / 100.0;
        promTiempo = Math.round(promTiempo * 100.0) / 100.0;

        return new EstadisticasEventoResponse(
                eventoId,
                evento.getTitulo(),
                inscritos,
                finalizados,
                abandonos,
                promPuntaje,
                maxPuntaje,
                minPuntaje,
                promTiempo
        );
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

    private List<RankingUsuarioResponse> mapearARanking(List<Object[]> resultados) {
        List<RankingUsuarioResponse> ranking = new ArrayList<>();
        for (int i = 0; i < resultados.size(); i++) {
            Object[] row = resultados.get(i);
            // row[0] = nombre, row[1] = puntaje, row[2] = carrera
            ranking.add(new RankingUsuarioResponse(
                    i + 1,
                    (String) row[0],
                    (Double) row[1],
                    (String) row[2]
            ));
        }
        return ranking;
    }
}