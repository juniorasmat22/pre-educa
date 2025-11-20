package com.bootcodeperu.admision_academica.domain.exception;

public class DomainValidationException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DomainValidationException(String message) {
        super(message);
    }
}
