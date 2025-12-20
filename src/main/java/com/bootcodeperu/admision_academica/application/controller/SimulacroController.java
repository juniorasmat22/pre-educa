package com.bootcodeperu.admision_academica.application.controller;

import java.util.List;
import java.util.Map;

import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaDetalleResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.resultadosimulacro.ResultadoSimulacroResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}