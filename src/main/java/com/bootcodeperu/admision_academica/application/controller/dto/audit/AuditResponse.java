package com.bootcodeperu.admision_academica.application.controller.dto.audit;

public record AuditResponse(
        String usuario,
        String fecha,
        String tipoCambio, // "Cambio de valor", "Objeto creado", etc.
        String propiedad,  // "nombre", "descripcion", etc.
        Object valorAnterior,
        Object valorNuevo
) {
}