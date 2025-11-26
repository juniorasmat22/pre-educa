package com.bootcodeperu.admision_academica.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.ContenidoTeoria;
import com.bootcodeperu.admision_academica.application.controller.dto.contenido.ContenidoTeoriaResponse;
import com.bootcodeperu.admision_academica.application.service.ContenidoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/contenido")
@RequiredArgsConstructor
public class ContenidoController {
    
    private final ContenidoService contenidoService;

    // GET /api/v1/contenido/temas/1/teoria
    @GetMapping("/temas/{temaId}/teoria")
    @PreAuthorize("hasAuthority('CONTENIDO_READ') or hasRole('ADMIN')") // Permiso necesario
    public ResponseEntity<List<ContenidoTeoriaResponse>> getTeoriaByTema(@PathVariable Long temaId) {
        List<ContenidoTeoriaResponse> teoria = contenidoService.getContenidoTeoriaByTemaId(temaId);
        return ResponseEntity.ok(teoria);
    }
}
