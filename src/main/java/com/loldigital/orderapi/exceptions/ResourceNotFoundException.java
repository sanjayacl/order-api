package com.loldigital.orderapi.exceptions;

public class ResourceNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
    public static final String ERROR_MESSAGE = "The resource not found!";
    
    public ResourceNotFoundException() {
    	super(ERROR_MESSAGE);
    }
    
    public ResourceNotFoundException(String message) {
    	super(message);
    }
   
}
