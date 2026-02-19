package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.analitica.RankingUsuarioResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.PreguntaDetalleResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.simulacro.CrearSimulacroProgramadoRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.simulacro.SimulacroProgramadoResponse;

import com.bootcodeperu.admision_academica.application.service.SimulacroProgramadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/eventos-simulacro")
@RequiredArgsConstructor
public class SimulacroProgramadoController {

    private final SimulacroProgramadoService eventoService;

    // ==========================================
    // 👑 ZONA ADMINISTRADOR
    // ==========================================

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo el personal de la academia puede crearlo
    public ResponseEntity<SimulacroProgramadoResponse> crearEventoOficial(
            @RequestBody CrearSimulacroProgramadoRequest request) {

        SimulacroProgramadoResponse evento = eventoService.crearSimulacroOficial(
                request.areaId(),
                request.titulo(),
                request.fechaInicio(),
                request.fechaFin()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(evento);
    }

    // ==========================================
    // 🎓 ZONA ESTUDIANTE
    // ==========================================

    /**
     * Lista los exámenes oficiales que están próximos a darse o en curso en este momento.
     * Ideal para mostrar en el "Home" del aplicativo del alumno.
     */
    @GetMapping("/disponibles/{areaId}")
    public ResponseEntity<List<SimulacroProgramadoResponse>> getEventosDisponibles(@PathVariable Long areaId) {
        return ResponseEntity.ok(eventoService.listarEventosDisponibles(areaId));
    }

    /**
     * El alumno hace clic en "Entrar al Examen Oficial".
     * Genera la sesión EN_CURSO y devuelve las preguntas congeladas del evento.
     */
    @PostMapping("/{eventoId}/iniciar")
    //@PreAuthorize("#usuarioId == authentication.principal.id")
    public ResponseEntity<List<PreguntaDetalleResponse>> iniciarSimulacroOficial(
            @PathVariable Long eventoId,
            @RequestParam Long usuarioId) {

        List<PreguntaDetalleResponse> examen = eventoService.iniciarSimulacroOficial(usuarioId, eventoId);
        return ResponseEntity.ok(examen);
    }

    /**
     * Ver el ranking EXCLUSIVO de este evento.
     */
    @GetMapping("/{eventoId}/ranking")
    public ResponseEntity<List<RankingUsuarioResponse>> getRankingDelEvento(@PathVariable Long eventoId) {
        return ResponseEntity.ok(eventoService.obtenerRankingPorEvento(eventoId));
    }
}