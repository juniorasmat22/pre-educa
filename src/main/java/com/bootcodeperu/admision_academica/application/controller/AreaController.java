package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.area.AreaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.service.AreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/areas")
@RequiredArgsConstructor
public class AreaController {
    private final AreaService areaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AreaResponse>>> getAllAreas() {
        return ResponseEntity.ok(
                ApiResponse.ok(areaService.getAllAreas(), "Áreas obtenidas con éxito")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AreaResponse>> getAreaById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(areaService.findAreaById(id), "Área encontrada")
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AreaResponse>> createArea(@RequestBody @Valid AreaRequest request) {
        return ResponseEntity.ok(
                ApiResponse.created(areaService.createArea(request), "Área creada exitosamente")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AreaResponse>> updateArea(
            @PathVariable Long id,
            @RequestBody @Valid AreaRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(areaService.updateArea(id, request), "Área actualizada exitosamente")
        );
    }
}
