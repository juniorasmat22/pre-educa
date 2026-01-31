package com.bootcodeperu.admision_academica.application.controller.advice;

import com.bootcodeperu.admision_academica.application.controller.dto.error.ErrorResponse;
import com.bootcodeperu.admision_academica.domain.exception.ContentLoadingException;
import com.bootcodeperu.admision_academica.domain.exception.DomainValidationException;
import com.bootcodeperu.admision_academica.domain.exception.DuplicateResourceException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo de Recurso No Encontrado (404)
    @ExceptionHandler({ResourceNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // 2. Manejo de Validaciones de Dominio y Reglas de Negocio (400)
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponse> handleDomainValidation(DomainValidationException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // 3. Manejo de Errores de Validación de Spring (@Valid en DTOs) (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        // Convertimos el mapa de errores a un string para el mensaje general
        String message = "Error de validación en los campos: " + errors.toString();

        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    // 4. Manejo de Errores de Autenticación (401)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleAuthError(BadCredentialsException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas", request);
    }

    // 5. Manejo de Error en Carga de Contenidos (500)
    @ExceptionHandler(ContentLoadingException.class)
    public ResponseEntity<ErrorResponse> handleContentError(ContentLoadingException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno procesando contenido: " + ex.getMessage(), request);
    }

    // 6. Fallback General (Cualquier otro error no controlado) (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalError(Exception ex, HttpServletRequest request) {
        // Es buena práctica loguear el error real en la consola aquí
        ex.printStackTrace();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado. Contacte al soporte.", request);
    }
    // 7. Manejo de Recursos Duplicados (409 Conflict)
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }
    // Método auxiliar para construir la respuesta
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, status);
    }
}