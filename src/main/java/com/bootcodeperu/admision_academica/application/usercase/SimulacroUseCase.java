package com.bootcodeperu.admision_academica.application.usercase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.bootcodeperu.admision_academica.application.controller.dto.analitica.DebilidadTemaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.analitica.RankingUsuarioResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaDetalleResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.resultadosimulacro.ResultadoSimulacroResponse;
import com.bootcodeperu.admision_academica.application.service.ProgresoService;
import com.bootcodeperu.admision_academica.domain.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.PreguntaDetalle;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.repository.PreguntaDetalleMongoRepository;
import com.bootcodeperu.admision_academica.application.service.SimulacroService;
import com.bootcodeperu.admision_academica.domain.exception.ContentLoadingException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import com.bootcodeperu.admision_academica.domain.model.Area;
import com.bootcodeperu.admision_academica.domain.model.CursoArea;
import com.bootcodeperu.admision_academica.domain.model.MetadatoPregunta;
import com.bootcodeperu.admision_academica.domain.model.ResultadoSimulacro;
import com.bootcodeperu.admision_academica.domain.model.Usuario;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Inyecta las dependencias a través del constructor
public class SimulacroUseCase implements SimulacroService{
	// Repositorios de PostgreSQL
    private final AreaRepository areaRepository;
    private final CursoAreaRepository cursoAreaRepository;
    private final TemaRepository temaRepository;
    private final MetadatoPreguntaRepository metadatoPreguntaRepository;
    private final ResultadoSimulacroRepository resultadoSimulacroRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper; // <<< INYECCIÓN
    private final ObjectMapper objectMapper; // <<< INYECCIÓN para JSONB
    private final ProgresoTemaRepository progresoTemaRepository;
    private final ProgresoService progresoService;
    // Repositorio de MongoDB
    private final PreguntaDetalleMongoRepository preguntaDetalleMongoRepository;

    /**
     * PASO 1: Genera un examen simulacro completo para un área de postulación.
     */
	@Override
	public List<PreguntaDetalleResponse> generarExamenSimulacro(Long areaId) {
		// 1. Verificar Área
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new ResourceNotFoundException("Área", "id", areaId));

        // 2. Obtener distribución de preguntas (PostgreSQL)
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
                    .findRandomByTemaIdInAndTipoPregunta(
                            temaIds,
                            "BancoSimulacro",
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

        // 4. Obtener detalles de las preguntas (MongoDB)
        // Se asegura el orden de las preguntas del examen
        Map<String, PreguntaDetalle> preguntasMap = preguntaDetalleMongoRepository
                .findAllByIdIn(mongoIdsSeleccionados).stream()
                .collect(Collectors.toMap(PreguntaDetalle::getId, p -> p));
        
        // Mapear los IDs seleccionados al orden del examen

        return mongoIdsSeleccionados.stream()
                .map(preguntasMap::get)
                .filter(Objects::nonNull) // Filtrar si alguna pregunta no se encontró en Mongo (error de datos)
                .map(p -> modelMapper.map(p, PreguntaDetalleResponse.class))
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

        // 2. Obtener Preguntas Detalle de Mongo (Solo las preguntas respondidas)
        List<String> mongoIdsRespondidos = new ArrayList<>(respuestas.keySet());
        List<PreguntaDetalle> detalles = preguntaDetalleMongoRepository.findAllByIdIn(mongoIdsRespondidos);
        
        // Si no se encuentran los detalles, no podemos evaluar (error de datos)
        if (detalles.isEmpty()) {
            throw new ContentLoadingException("No se encontraron detalles de las preguntas para la evaluación.");
        }

        // 3. Lógica de Puntuación (EJEMPLO SIMPLIFICADO)
     // Obtener los pesos del Área
        final double pesoCorrecta = area.getPuntajeCorrecta();    // Ej: 4.00
        final double pesoIncorrecta = area.getPuntajeIncorrecta(); // Ej: -0.25,
        final double pesoBlanco = area.getPuntajeCorrecta(); // Ej: -0.25,
        // pero aquí se usa un puntaje base simple.
        double puntajeTotal = 0.0;
        int preguntasCorrectas = 0;

        // Crear el JSON para detallesRespuestas
        List<Map<String, Object>> detallesRespuestasList = new ArrayList<>();
        for (PreguntaDetalle detalle : detalles) {
            String respuestaDada = respuestas.get(detalle.getId());
            boolean esCorrecta = detalle.getRespuestaCorrecta().equalsIgnoreCase(respuestaDada);
            double puntajePregunta;

            if (esCorrecta) {
                puntajePregunta = pesoCorrecta;
            } else if (respuestaDada != null) {
                // Si la respuesta no es nula (marcó incorrectamente)
                puntajePregunta = pesoIncorrecta;
            } else {
                // Pregunta no respondida
                puntajePregunta = 0.0;
            }

            puntajeTotal += puntajePregunta;
            if (detalle.getIdTemaSQL() != null) {
                // Enviamos 1.0 si es correcta, 0.0 si es incorrecta.
                // El ProgresoService se encarga de promediarlo con los intentos anteriores.
                progresoService.actualizarPuntajePromedio(usuarioId, detalle.getIdTemaSQL(), esCorrecta ? 1.0 : 0.0);
            }
            // Construir el objeto de detalle para guardar
            Map<String, Object> detalleRespuesta = new HashMap<>();
            detalleRespuesta.put("mongoId", detalle.getId());
            detalleRespuesta.put("respuestaDada", respuestaDada);
            detalleRespuesta.put("esCorrecta", esCorrecta);
            detalleRespuesta.put("puntajeObtenido", puntajePregunta);
            detallesRespuestasList.add(detalleRespuesta);
        }
        
        // 4. Guardar Resultado en PostgreSQL
        ResultadoSimulacro resultado = new ResultadoSimulacro();
        resultado.setUsuario(usuario);
        resultado.setAreaEvaluada(area);
        resultado.setTiempoTomado(tiempoTomado);
        resultado.setPuntajeTotal(puntajeTotal);

        // Mapear la lista de Map a JsonNode usando ObjectMapper para el campo JSONB/JdbcTypeCode(SqlTypes.JSON)
        // Este paso es crucial para persistir el detalle del JSON en PostgreSQL.
        JsonNode detallesJson = objectMapper.valueToTree(detallesRespuestasList);
        resultado.setDetallesRespuestas(detallesJson);

        ResultadoSimulacro resultadoGuardado = resultadoSimulacroRepository.save(resultado);
        // Retorna el resultado (asumiendo que los campos están configurados correctamente para JPA)
        // 5. Devolver DTO (Mapeo)
        return modelMapper.map(resultadoGuardado, ResultadoSimulacroResponse.class);
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

    @Override
    public List<RankingUsuarioResponse> obtenerRankingPorArea(Long areaId) {
        List<Object[]> resultados = resultadoSimulacroRepository.findTop10ByArea(areaId);
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
}
