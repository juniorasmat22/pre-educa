package com.bootcodeperu.admision_academica.domain.exception;
/**
 * Excepción para indicar que ya existe un registro con un valor único duplicado.
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException() {
        super();
    }

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
