package com.bootcodeperu.admision_academica.application.controller.dto.error;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {}