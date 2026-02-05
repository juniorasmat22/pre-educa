package com.bootcodeperu.admision_academica.application.controller.dto.common;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        LocalDateTime timestamp,
        boolean success,
        int status,
        String message,
        T data
) {
    // 1. Constructor estático para respuestas EXITOSAS con DATOS (GET, POST con retorno)
    public static <T> ApiResponse<T> success(T data, String message, int status) {
        return new ApiResponse<>(LocalDateTime.now(), true, status, message, data);
    }

    // 2. Constructor estático para respuestas EXITOSAS SIN DATOS (DELETE, PUT)
    public static <T> ApiResponse<T> empty(String message, int status) {
        return new ApiResponse<>(LocalDateTime.now(), true, status, message, null);
    }

    // 3. Constructor estático por defecto (200 OK)
    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(LocalDateTime.now(), true, 200, message, data);
    }

    // 4. Constructor estático para Creación (201 Created)
    public static <T> ApiResponse<T> created(T data, String message) {
        return new ApiResponse<>(LocalDateTime.now(), true, 201, message, data);
    }
}