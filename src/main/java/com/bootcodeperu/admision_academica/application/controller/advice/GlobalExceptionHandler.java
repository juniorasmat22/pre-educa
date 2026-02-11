package com.bootcodeperu.admision_academica.application.controller.advice;

import com.bootcodeperu.admision_academica.application.controller.dto.error.ErrorResponse;
import com.bootcodeperu.admision_academica.domain.exception.ContentLoadingException;
import com.bootcodeperu.admision_academica.domain.exception.DomainValidationException;
import com.bootcodeperu.admision_academica.domain.exception.DuplicateResourceException;
import com.bootcodeperu.admision_academica.domain.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /* =====================================================
       401 - ESTADO DE CUENTA
       ===================================================== */
    @ExceptionHandler({
            DisabledException.class,
            LockedException.class,
            AccountExpiredException.class,
            CredentialsExpiredException.class
    })
    public ResponseEntity<ErrorResponse> handleAccountStatus(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        String message;

        if (ex instanceof DisabledException) {
            message = "La cuenta está deshabilitada";
        } else if (ex instanceof LockedException) {
            message = "La cuenta está bloqueada";
        } else if (ex instanceof AccountExpiredException) {
            message = "La cuenta ha expirado";
        } else {
            message = "Las credenciales han expirado";
        }

        return buildResponse(HttpStatus.UNAUTHORIZED, message, request);
    }

    /* =====================================================
       404 - RECURSOS NO ENCONTRADOS
       ===================================================== */
    @ExceptionHandler({
            ResourceNotFoundException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /* =====================================================
       409 - RECURSOS DUPLICADOS
       ===================================================== */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(
            DuplicateResourceException ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /* =====================================================
       409 - ESTADO ILEGAL DE LA OPERACIÓN
       ===================================================== */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(
            IllegalStateException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request
        );
    }

    /* =====================================================
       409 - ERROR DE CONCURRENCIA (Optimistic Lock)
       ===================================================== */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleConcurrency(
            ObjectOptimisticLockingFailureException ex,
            HttpServletRequest request
    ) {
        log.error("Error de concurrencia: Otro usuario actualizó este registro.", ex);

        return buildResponse(
                HttpStatus.CONFLICT,
                "Los datos fueron modificados por otro usuario. Recarga la página.",
                request
        );
    }

    /* =====================================================
       400 - VALIDACIONES DE DOMINIO
       ===================================================== */
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponse> handleDomainValidation(
            DomainValidationException ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /* =====================================================
       400 - VALIDACIONES DE DTO (@Valid)
       ===================================================== */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleDtoValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        String message = "Error de validación en los campos: " + fieldErrors;
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /* =====================================================
       401 - AUTENTICACIÓN
       ===================================================== */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                "Credenciales incorrectas",
                request
        );
    }

    // Manejo de Token Expirado (401 Unauthorized)
    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(io.jsonwebtoken.ExpiredJwtException ex, HttpServletRequest request) {
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                "Tu sesión ha expirado. Por favor, vuelve a iniciar sesión.",
                request
        );
    }

    // Manejo de Token Inválido o Malformado (401 Unauthorized)
    @ExceptionHandler({
            io.jsonwebtoken.security.SignatureException.class,
            io.jsonwebtoken.MalformedJwtException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidJwtException(Exception ex, HttpServletRequest request) {
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                "El token de seguridad es inválido.",
                request
        );
    }

    /* =====================================================
       500 - ERRORES DE CARGA DE CONTENIDO
       ===================================================== */
    @ExceptionHandler(ContentLoadingException.class)
    public ResponseEntity<ErrorResponse> handleContentError(
            ContentLoadingException ex,
            HttpServletRequest request
    ) {
        log.error("Error cargando contenido", ex);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request
        );
    }

    /* =====================================================
       500 - FALLBACK GENERAL
       ===================================================== */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("Error inesperado", ex);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado. Contacte al soporte.",
                request
        );
    }

    /* =====================================================
       MÉTODO AUXILIAR
       ===================================================== */
    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, request.getRequestURI());

        return new ResponseEntity<>(errorResponse, status);
    }
}