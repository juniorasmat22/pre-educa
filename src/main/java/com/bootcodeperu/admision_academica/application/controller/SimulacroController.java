package com.bootcodeperu.admision_academica.application.controller;

import java.util.List;
import java.util.Map;

import com.bootcodeperu.admision_academica.application.controller.dto.analitica.*;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaDetalleResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.resultadosimulacro.ResultadoSimulacroResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bootcodeperu.admision_academica.application.service.SimulacroService;

import lombok.RequiredArgsConstructor;

//Clase DTO para la solicitud de evaluación (necesario para estructurar el JSON de entrada)
record EvaluacionRequest(Long usuarioId, Long areaId, Map<String, String> respuestas, Integer tiempoTomado) {}

@RestController
@RequestMapping("/api/v1/simulacros")
@RequiredArgsConstructor
public class SimulacroController {

 private final SimulacroService simulacroService;

 // GET /api/v1/simulacros/generar?areaId=1
 @GetMapping("/generar")
 public ResponseEntity<List<PreguntaDetalleResponse>> generarSimulacro(@RequestParam Long areaId) {
     List<PreguntaDetalleResponse> examen = simulacroService.generarExamenSimulacro(areaId);
     return ResponseEntity.ok(examen);
 }
 // POST /api/v1/simulacros/evaluar
 @PostMapping("/evaluar")
 public ResponseEntity<ResultadoSimulacroResponse> evaluarSimulacro(@RequestBody EvaluacionRequest request) {
     ResultadoSimulacroResponse resultado = simulacroService.evaluarSimulacro(
             request.usuarioId(),
             request.areaId(),
             request.respuestas(),
             request.tiempoTomado()
     );
     return ResponseEntity.ok(resultado);
 }
    /**
     * Devuelve los temas donde el alumno está fallando más
     */
    @GetMapping("/mis-debilidades/{usuarioId}")
    @PreAuthorize("#usuarioId == authentication.principal.id")
    public ResponseEntity<List<DebilidadTemaResponse>> getDebilidades(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(simulacroService.obtenerAnalisisDebilidades(usuarioId));
    }
    @GetMapping("/ranking/global")
    public ResponseEntity<List<RankingUsuarioResponse>> getTopGlobal() {
        return ResponseEntity.ok(simulacroService.obtenerTop10GlobalSemanal());
    }

    @GetMapping("/ranking/area/{areaId}")
    public ResponseEntity<List<RankingUsuarioResponse>> getTopByArea(@PathVariable Long areaId) {
        return ResponseEntity.ok(simulacroService.obtenerRankingPorArea(areaId));
    }
    @GetMapping("/historial-evolucion/{usuarioId}")
    @PreAuthorize("#usuarioId == authentication.principal.id")
    public ResponseEntity<List<EvolucionPuntajeResponse>> getEvolucion(@PathVariable Long usuarioId) {
        List<EvolucionPuntajeResponse> evolucion = simulacroService.obtenerEvolucionEstudiante(usuarioId);
        return ResponseEntity.ok(evolucion);
    }
    @GetMapping("/analitica/comparativa/{usuarioId}/{areaId}")
    @PreAuthorize("#usuarioId == authentication.principal.id")
    public ResponseEntity<EstadisticaComparativaResponse> getComparativa(
            @PathVariable Long usuarioId,
            @PathVariable Long areaId) {
        return ResponseEntity.ok(simulacroService.obtenerPercentilEstudiante(usuarioId, areaId));
    }
    @GetMapping("/ruta-aprendizaje/{usuarioId}")
    @PreAuthorize("#usuarioId == authentication.principal.id")
    public ResponseEntity<List<RecomendacionResponse>> getRutaPersonalizada(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(simulacroService.generarRutaRecomendada(usuarioId));
    }
}