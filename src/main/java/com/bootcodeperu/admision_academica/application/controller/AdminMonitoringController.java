package com.bootcodeperu.admision_academica.application.controller;

import com.bootcodeperu.admision_academica.application.controller.dto.audit.AuditResponse;
import com.bootcodeperu.admision_academica.application.controller.dto.common.ApiResponse;
import com.bootcodeperu.admision_academica.application.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/monitoring")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminMonitoringController {
    private final HealthEndpoint healthEndpoint; // Inyectado por Actuator
    private final AuditService auditService;

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Object>> getSystemStatus() {
        // Devuelve el estado de Salud (Mongo, Postgres, Disco)
        return ResponseEntity.ok(ApiResponse.ok(healthEndpoint.health(), "Estado del sistema obtenido"));
    }

    @GetMapping("/audit/{entityId}")
    public ResponseEntity<ApiResponse<List<AuditResponse>>> getEntityAudit(
            @PathVariable Object entityId,
            @RequestParam String className) throws ClassNotFoundException {

        // Convertimos el string a clase (Ej: "Area" -> Area.class)
        Class<?> clazz = Class.forName("com.bootcodeperu.admision_academica.domain.model." + className);
        Object idFinal;
        try {
            idFinal = Long.parseLong(entityId.toString());
        } catch (NumberFormatException e) {
            idFinal = entityId; // Si falla, lo dejamos como String (caso de MongoDB)
        }
        return ResponseEntity.ok(
                ApiResponse.ok(auditService.getHistory(idFinal, clazz), "Historial recuperado")
        );
    }
}