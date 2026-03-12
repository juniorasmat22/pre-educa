package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.catalogo.CatalogoResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.service.CatalogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogo") // 🌟 Ruta diferente a la de administración
@RequiredArgsConstructor
public class CatalogoPublicoController {

    private final CatalogoService catalogoService;

    @GetMapping("/universidades")
    public ResponseEntity<ApiResponse<List<CatalogoResponse>>> obtenerCatalogo() {
        List<CatalogoResponse> data = catalogoService.obtenerCatalogoCompleto();
        return ResponseEntity.ok(ApiResponse.ok(data, "Catálogo académico obtenido correctamente"));
    }
}
