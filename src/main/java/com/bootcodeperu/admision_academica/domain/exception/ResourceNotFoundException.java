package com.bootcodeperu.admision_academica.domain.exception;

public class ResourceNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Constructor simple
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    // Constructor útil para indicar qué recurso faltó
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s no encontrado con %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
