package com.bootcodeperu.admision_academica.application.usercase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.bootcodeperu.admision_academica.adapter.mapper.PreguntaDetalleMapper;
import com.bootcodeperu.admision_academica.adapter.mapper.ResultadoSimulacroMapper;
import com.bootcodeperu.admision_academica.application.controller.dto.analitica.*;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaDetalleResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.resultadosimulacro.ResultadoSimulacroResponse;
import com.bootcodeperu.admision_academica.application.service.ProgresoService;
import com.bootcodeperu.admision_academica.domain.exception.BusinessException;
import com.bootcodeperu.admision_academica.domain.model.*;
import com.bootcodeperu.admision_academica.domain.model.enums.EstadoSimulacro;
import com.bootcodeperu.admision_academica.domain.model.enums.QuestionTarget;
import com.bootcodeperu.admision_academica.domain.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.PreguntaDetalle;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.repository.PreguntaDetalleMongoRepository;
import com.bootcodeperu.admision_academica.application.service.SimulacroService;
import com.bootcodeperu.admision_academica.domain.exception.ContentLoadingException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Inyecta las dependencias a través del constructor
public class SimulacroUseCase implements SimulacroService {
    // Repositorios de PostgreSQL
    private final AreaRepository areaRepository;
    private final CursoAreaRepository cursoAreaRepository;
    private final TemaRepository temaRepository;
    private final MetadatoPreguntaRepository metadatoPreguntaRepository;
    private final ResultadoSimulacroRepository resultadoSimulacroRepository;
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper; // <<< INYECCIÓN para JSONB
    private final ProgresoTemaRepository progresoTemaRepository;
    private final ProgresoService progresoService;
    // Repositorio de MongoDB
    private final PreguntaDetalleMongoRepository preguntaDetalleMongoRepository;

    private final PreguntaDetalleMapper preguntaDetalleMapper;
    private final ResultadoSimulacroMapper resultadoSimulacroMapper;

    /**
     * PASO 1: Genera un examen simulacro completo para un área de postulación.
     */
    @Override
    public List<PreguntaDetalleResponse> generarExamenSimulacro(Long usuarioId, Long areaId) {
        // 1. CONTROL DE INTENTOS: No permitir si ya tiene uno EN_CURSO
        if (resultadoSimulacroRepository.existsByUsuarioIdAndEstado(usuarioId, EstadoSimulacro.EN_CURSO)) {
            throw new BusinessException("Ya tienes un simulacro en curso. Debes finalizarlo antes.");
        }
        // 2. Verificar Usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", areaId));

        // 2. Verificar Área
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new ResourceNotFoundException("Área", "id", areaId));

        // 3. Obtener distribución de preguntas (PostgreSQL)
        List<CursoArea> distribucion = cursoAreaRepository.findAllByAreaId(areaId);
        if (distribucion.isEmpty()) {
            throw new ContentLoadingException("No se encontró distribución de cursos para el área: " + area.getNombre());
        }

        // Lista de IDs de Mongo para las preguntas seleccionadas
        List<String> mongoIdsSeleccionados = new ArrayList<>();

        // 3. Seleccionar preguntas por Curso (PostgreSQL)
        for (CursoArea ca : distribucion) {
            Long cursoId = ca.getCurso().getId();
            int cantidadRequerida = ca.getCantidadPreguntas();

            // Obtener los IDs de los temas de ese curso (necesario para la consulta eficiente)
            List<Long> temaIds = temaRepository.findIdsByCursoId(cursoId);

            if (temaIds.isEmpty()) continue;

            // Seleccionar IDs de Metadatos de forma aleatoria (tipo BancoSimulacro)
            List<MetadatoPregunta> metadatosSeleccionados = metadatoPreguntaRepository
                    .findRandomByTemaIdInAndTarget(
                            temaIds,
                            QuestionTarget.EXAM.name(),
                            cantidadRequerida
                    );

            // Agregar los IDs de Mongo de las preguntas seleccionadas
            mongoIdsSeleccionados.addAll(
                    metadatosSeleccionados.stream()
                            .map(MetadatoPregunta::getMongoIdPregunta)
                            .toList()
            );
        }

        if (mongoIdsSeleccionados.isEmpty()) {
            throw new ContentLoadingException("No se pudieron cargar preguntas de banco para el simulacro.");
        }
        ResultadoSimulacro nuevaSesion = new ResultadoSimulacro();
        nuevaSesion.setUsuario(usuario);
        nuevaSesion.setAreaEvaluada(area);
        nuevaSesion.setEstado(EstadoSimulacro.EN_CURSO);
        nuevaSesion.setFechaEvaluacion(LocalDateTime.now());
        nuevaSesion.setFechaExpiracion(LocalDateTime.now().plusMinutes(area.getDuracionMinutos()));

        resultadoSimulacroRepository.save(nuevaSesion);
        // 4. Obtener detalles de las preguntas (MongoDB)
        // Se asegura el orden de las preguntas del examen
        Map<String, PreguntaDetalle> preguntasMap = preguntaDetalleMongoRepository
                .findAllByIdIn(mongoIdsSeleccionados).stream()
                .collect(Collectors.toMap(PreguntaDetalle::getId, p -> p));

        // Mapear los IDs seleccionados al orden del examen

        return mongoIdsSeleccionados.stream()
                .map(preguntasMap::get)
                .filter(Objects::nonNull) // Filtrar si alguna pregunta no se encontró en Mongo (error de datos)
                .map(preguntaDetalleMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * PASO 2: Evalúa las respuestas y guarda el resultado.
     */
    @Override
    @Transactional // Asegura que la operación de guardar el resultado sea atómica
    public ResultadoSimulacroResponse evaluarSimulacro(Long usuarioId, Long areaId, Map<String, String> respuestas,
                                                       Integer tiempoTomado) {
        // 1. Verificar Usuario y Área
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new ResourceNotFoundException("Área", "id", areaId));
        // 2. RECUPERAR SESIÓN ACTIVA (Importante: No creamos una nueva, actualizamos la existente)
        ResultadoSimulacro sesion = resultadoSimulacroRepository
                .findByUsuarioIdAndEstado(usuarioId, EstadoSimulacro.EN_CURSO)
                .orElseThrow(() -> new BusinessException("No tienes un simulacro en curso para evaluar."));
        LocalDateTime ahora = LocalDateTime.now();
        // Definimos una gracia de 2 minutos sobre la fecha de expiración original
        LocalDateTime limiteConGracia = sesion.getFechaExpiracion().plusMinutes(2);

        if (ahora.isAfter(limiteConGracia)) {
            // Si llega después de la gracia, el Scheduler probablemente ya actuó o el intento es fraudulento
            throw new BusinessException("El tiempo de envío ha expirado excediendo la tolerancia permitida.");
        }
        // Calculamos si hubo retraso real para auditoría
        if (ahora.isAfter(sesion.getFechaExpiracion())) {
            //log.warn("Examen recibido con retraso. Usuario: {}, Retraso: {} segundos",
            //      usuarioId, java.time.Duration.between(sesion.getFechaExpiracion(), ahora).getSeconds());
        }
        // 3. Obtener Preguntas Detalle de Mongo (Solo las preguntas respondidas)
        List<String> mongoIdsRespondidos = new ArrayList<>(respuestas.keySet());
        List<PreguntaDetalle> detalles = preguntaDetalleMongoRepository.findAllByIdIn(mongoIdsRespondidos);

        // Si no se encuentran los detalles, no podemos evaluar (error de datos)
        if (detalles.isEmpty()) {
            throw new ContentLoadingException("No se encontraron detalles de las preguntas para la evaluación.");
        }

        // 4. Lógica de Puntuación (EJEMPLO SIMPLIFICADO)
        // Obtener los pesos del Área

        final double pesoCorrecta = area.getPuntajeCorrecta();
        final double pesoIncorrecta = area.getPuntajeIncorrecta();
        final double pesoEnBlanco = area.getPuntajeBlanco();

        double puntajeTotal = 0.0;
        int correctas = 0;
        int incorrectas = 0;
        int enBlanco = 0;

        List<Map<String, Object>> detallesRespuestasList = new ArrayList<>();

        for (PreguntaDetalle detalle : detalles) {
            String respuestaDada = respuestas.get(detalle.getId());
            boolean esCorrecta = detalle.getRespuestaCorrecta().equalsIgnoreCase(respuestaDada);
            double puntajePregunta;

            // Lógica de conteo y puntos
            if (respuestaDada == null || respuestaDada.trim().isEmpty()) {
                puntajePregunta = pesoEnBlanco;
                enBlanco++;
            } else if (esCorrecta) {
                puntajePregunta = pesoCorrecta;
                correctas++;
            } else {
                puntajePregunta = pesoIncorrecta;
                incorrectas++;
            }

            puntajeTotal += puntajePregunta;

            // Actualizar progreso académico
            if (detalle.getIdTemaSQL() != null) {
                progresoService.actualizarPuntajePromedio(usuarioId, detalle.getIdTemaSQL(), esCorrecta ? 1.0 : 0.0);
            }

            // Detalle JSONB
            Map<String, Object> d = new HashMap<>();
            d.put("mongoId", detalle.getId());
            d.put("respuestaDada", respuestaDada);
            d.put("esCorrecta", esCorrecta);
            d.put("puntajeObtenido", puntajePregunta);
            detallesRespuestasList.add(d);
        }

        // 5. ACTUALIZAR CAMPOS DE LA SESIÓN EXISTENTE
        sesion.setTiempoTomado(tiempoTomado);
        sesion.setPuntajeTotal(puntajeTotal);
        sesion.setPreguntasCorrectas(correctas);
        sesion.setPreguntasIncorrectas(incorrectas);
        sesion.setPreguntasEnBlanco(enBlanco);
        sesion.setEstado(EstadoSimulacro.FINALIZADO); // Cerramos el examen

        // Mapear la lista de Map a JsonNode usando ObjectMapper para el campo JSONB/JdbcTypeCode(SqlTypes.JSON)
        // Este paso es crucial para persistir el detalle del JSON en PostgreSQL.
        JsonNode detallesJson = objectMapper.valueToTree(detallesRespuestasList);
        sesion.setDetallesRespuestas(detallesJson);

        // Guardar cambios
        ResultadoSimulacro resultadoGuardado = resultadoSimulacroRepository.save(sesion);

        return resultadoSimulacroMapper.toResponse(resultadoGuardado); //modelMapper.map(resultadoGuardado, ResultadoSimulacroResponse.class);
    }

    @Override
    public List<DebilidadTemaResponse> obtenerAnalisisDebilidades(Long usuarioId) {
        // 1. La base de datos ya nos da SOLO lo que necesitamos (menos de 60%)
        // Usamos 0.6 como umbral
        return progresoTemaRepository.findByUsuarioIdAndPuntajePromedioLessThan(usuarioId, 0.6)
                .stream()
                .map(p -> new DebilidadTemaResponse(
                        p.getTema().getCurso().getNombre(),
                        p.getTema().getNombreTema(),
                        p.getPuntajePromedio() * 100,
                        p.getPuntajePromedio() < 0.4 ? "REFORZAR TEORÍA URGENTE" : "PRACTICAR MÁS EJERCICIOS"
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<RankingUsuarioResponse> obtenerTop10GlobalSemanal() {
        LocalDateTime haceSieteDias = LocalDateTime.now().minusDays(7);

        // Obtenemos los resultados destacados
        List<Object[]> resultados = resultadoSimulacroRepository.findTop10Global(haceSieteDias);

        return mapearARanking(resultados);
    }

    //    @Override
//    public List<RankingUsuarioResponse> obtenerRankingPorArea(Long areaId) {
//        List<Object[]> resultados = resultadoSimulacroRepository.findTop10ByArea(areaId);
//        return mapearARanking(resultados);
//    }
    @Override
    public List<RankingUsuarioResponse> obtenerRankingPorArea(Long areaId) {
        // Usamos la nueva query nativa con DENSE_RANK y criterios de desempate
        List<Object[]> resultados = resultadoSimulacroRepository.findRankingOficialByArea(areaId);
        return mapearARanking(resultados);
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

    @Override
    public List<EvolucionPuntajeResponse> obtenerEvolucionEstudiante(Long usuarioId) {
        // 1. Buscamos todos los resultados del usuario ordenados por fecha
        List<ResultadoSimulacro> historial = resultadoSimulacroRepository.findByUsuarioId(usuarioId);

        // 2. Mapeamos a nuestro DTO de evolución
        return historial.stream()
                .map(r -> new EvolucionPuntajeResponse(
                        r.getFechaEvaluacion(),
                        r.getPuntajeTotal(),
                        r.getAreaEvaluada().getNombre()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public EstadisticaComparativaResponse obtenerPercentilEstudiante(Long usuarioId, Long areaId) {
        // 1. Obtener el último puntaje del usuario
        ResultadoSimulacro ultimo = resultadoSimulacroRepository
                .findTopByUsuarioIdOrderByFechaEvaluacionDesc(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Resultado", "usuario", usuarioId));

        Double miPuntaje = ultimo.getPuntajeTotal();

        // 2. Obtener todos los puntajes del área (ordenados)
        List<Double> todosLosPuntajes = resultadoSimulacroRepository.findAllPuntajesByArea(areaId);

        if (todosLosPuntajes.isEmpty()) {
            return new EstadisticaComparativaResponse(miPuntaje, miPuntaje, 100.0, "Eres el primer postulante en esta área.");
        }

        // 3. Calcular posición (Percentil)
        long menores = todosLosPuntajes.stream().filter(p -> p < miPuntaje).count();
        long iguales = todosLosPuntajes.stream().filter(p -> p.equals(miPuntaje)).count();
        int total = todosLosPuntajes.size();

        double percentil = ((menores + (0.5 * iguales)) / total) * 100;
        double promedioArea = todosLosPuntajes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        // 4. Generar mensaje motivador
        String mensaje;
        if (percentil >= 90) mensaje = "¡Nivel Excelente! Estás en el top 10% de tu área.";
        else if (percentil >= 70) mensaje = "Muy buen nivel. Estás por encima del promedio competitivo.";
        else if (percentil >= 50) mensaje = "Nivel promedio. Aumenta tus horas de práctica para asegurar tu ingreso.";
        else mensaje = "Nivel bajo. Enfócate en tus debilidades detectadas en el análisis.";

        return new EstadisticaComparativaResponse(miPuntaje, promedioArea, Math.round(percentil * 100.0) / 100.0, mensaje);
    }

    @Override
    public List<RecomendacionResponse> generarRutaRecomendada(Long usuarioId) {
        List<RecomendacionResponse> recomendaciones = new ArrayList<>();

        // 1. Obtener debilidades críticas (Menos del 40% de acierto)
        List<ProgresoTema> debilidadesCriticas = progresoTemaRepository
                .findByUsuarioIdAndPuntajePromedioLessThan(usuarioId, 0.4);

        for (ProgresoTema p : debilidadesCriticas) {
            recomendaciones.add(new RecomendacionResponse(
                    "ALTA",
                    "Refuerzo Urgente: " + p.getTema().getNombreTema(),
                    "Tu precisión es menor al 40%. Te recomendamos revisar la teoría nuevamente.",
                    p.getTema().getId(),
                    p.getTema().getCurso().getNombre()
            ));
        }

        // 2. Analizar tendencia negativa (Si el último simulacro fue peor que el anterior en un tema)
        // Esto es "IA" básica: detección de patrones de retroceso
        List<ResultadoSimulacro> ultimosDos = resultadoSimulacroRepository
                .findTop2ByUsuarioIdOrderByFechaEvaluacionDesc(usuarioId);

        if (ultimosDos.size() == 2) {
            // Aquí podrías comparar el JSONB de ambos resultados para ver qué temas bajaron
            // Por simplicidad, añadimos una recomendación de consistencia
            recomendaciones.add(new RecomendacionResponse(
                    "MEDIA",
                    "Mantén el ritmo",
                    "Has realizado 2 simulacros esta semana. ¡Sigue así para mejorar tu percentil!",
                    null,
                    "General"
            ));
        }

        return recomendaciones;
    }

    @Transactional
    public void guardarProgreso(Long usuarioId, Map<String, String> respuestasParciales) {
        ResultadoSimulacro sesion = resultadoSimulacroRepository
                .findByUsuarioIdAndEstado(usuarioId, EstadoSimulacro.EN_CURSO)
                .orElseThrow(() -> new BusinessException("No hay un examen activo para guardar progreso."));

        // Convertimos el Map de respuestas actuales a JsonNode para actualizar el borrador
        JsonNode progresoJson = objectMapper.valueToTree(respuestasParciales);
        sesion.setDetallesRespuestas(progresoJson);

        resultadoSimulacroRepository.save(sesion);
    }
}
