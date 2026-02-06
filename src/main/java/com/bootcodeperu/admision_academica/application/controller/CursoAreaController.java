package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CreateCursoAreaRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CursoAreaResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.CursoAreaSimpleResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.curso_area.UpdateCursoAreaRequest;
import com.bootcodeperu.admision_academica.application.service.CursoAreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/curso-area")
@RequiredArgsConstructor
public class CursoAreaController {
    private final CursoAreaService cursoAreaService;

    // Crear una distribución curso-area
    @PostMapping
    public ResponseEntity<ApiResponse<CursoAreaResponse>> create(@RequestBody @Valid CreateCursoAreaRequest request) {
        CursoAreaResponse response = cursoAreaService.create(request);
        return ResponseEntity.ok(ApiResponse.created(response, "Distribución creada correctamente"));
    }

    // Obtener por ID compuesto (cursoId + areaId)
    @GetMapping("/curso/{cursoId}/area/{areaId}")
    public ResponseEntity<ApiResponse<CursoAreaSimpleResponse>> findById(
            @PathVariable Long cursoId,
            @PathVariable Long areaId) {

        CursoAreaSimpleResponse response = cursoAreaService.findById(cursoId, areaId);
        return ResponseEntity.ok(ApiResponse.ok(response, "Distribución encontrada"));
    }

    // Listar todas las distribuciones de un área
    @GetMapping("/area/{areaId}")
    public ResponseEntity<ApiResponse<List<CursoAreaSimpleResponse>>> findByArea(@PathVariable Long areaId) {
        List<CursoAreaSimpleResponse> responses = cursoAreaService.findByArea(areaId);
        return ResponseEntity.ok(ApiResponse.ok(responses, "Distribuciones obtenidas correctamente"));
    }

    // Actualizar la cantidad de preguntas de un CursoArea
    @PutMapping("/curso/{cursoId}/area/{areaId}")
    public ResponseEntity<ApiResponse<CursoAreaResponse>> update(
            @PathVariable Long cursoId,
            @PathVariable Long areaId,
            @RequestBody @Valid UpdateCursoAreaRequest request) {

        CursoAreaResponse response = cursoAreaService.update(cursoId, areaId, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Distribución actualizada correctamente"));
    }

    // Eliminar un CursoArea
    @DeleteMapping("/curso/{cursoId}/area/{areaId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long cursoId,
            @PathVariable Long areaId) {

        cursoAreaService.delete(cursoId, areaId);
        return ResponseEntity.ok(ApiResponse.ok(null, "Distribución eliminada correctamente"));
    }

}
