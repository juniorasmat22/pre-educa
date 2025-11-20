package com.bootcodeperu.admision_academica.domain.exception;

public class ContentLoadingException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContentLoadingException(String message) {
        super(message);
    }
    
    public ContentLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
