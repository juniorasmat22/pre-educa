package com.bootcodeperu.admision_academica.application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcodeperu.admision_academica.application.controller.dto.progreso.ProgresoUpdateRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.progresotema.ProgresoTemaResponse;
import com.bootcodeperu.admision_academica.application.service.ProgresoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/progreso")
@RequiredArgsConstructor
public class ProgresoController {
	private final ProgresoService progresoService;

    /**
     * Endpoint para obtener todo el progreso de un usuario.
     * GET /api/v1/progreso/usuarios/{userId}
     * * Seguridad: Permite solo al usuario autenticado (principal) ver su propio progreso 
     * o a un administrador.
     */
    @GetMapping("/usuarios/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id") 
    public ResponseEntity<List<ProgresoTemaResponse>> getProgreso(
            @PathVariable Long userId) {
        
        List<ProgresoTemaResponse> progresoList = progresoService.getProgresoByUsuario(userId);
        return ResponseEntity.ok(progresoList);
    }

    /**
     * Endpoint para actualizar el puntaje promedio de práctica de un tema.
     * PUT /api/v1/progreso/usuarios/{userId}/practica
     * * Seguridad: Solo permite al usuario autenticado modificar su propio progreso.
     */
    @PutMapping("/usuarios/{userId}/practica")
    @PreAuthorize("#userId == authentication.principal.id") // Solo el propietario puede actualizar
    public ResponseEntity<ProgresoTemaResponse> actualizarProgresoPractica(
            @PathVariable Long userId,
            @RequestBody ProgresoUpdateRequest request) {
        
        ProgresoTemaResponse progresoActualizado = progresoService.actualizarPuntajePromedio(
                userId,
                request.temaId(),
                request.nuevoPuntaje()
        );
        
        return new ResponseEntity<>(progresoActualizado, HttpStatus.OK);
    }
}
