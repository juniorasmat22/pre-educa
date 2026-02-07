package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.carrera.CarreraRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.carrera.CarreraResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.service.CarreraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carerra")
@RequiredArgsConstructor
public class CarreraController {
    private final CarreraService carreraService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CarreraResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(carreraService.getAllCareers(), "Lista de carreras obtenida"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CarreraResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(carreraService.getCareerById(id), "Detalles de carrera encontrados"));
    }

    @GetMapping("/area/{areaId}")
    public ResponseEntity<ApiResponse<List<CarreraResponse>>> getByArea(@PathVariable Long areaId) {
        return ResponseEntity.ok(ApiResponse.ok(carreraService.getCareersByAreaId(areaId), "Carreras por área obtenidas"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CarreraResponse>> create(@RequestBody @Valid CarreraRequest request) {
        return ResponseEntity.ok(ApiResponse.created(carreraService.createCareer(request), "Carrera creada exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CarreraResponse>> update(@PathVariable Long id, @RequestBody @Valid CarreraRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(carreraService.updateCareer(id, request), "Carrera actualizada con éxito"));
    }
}
