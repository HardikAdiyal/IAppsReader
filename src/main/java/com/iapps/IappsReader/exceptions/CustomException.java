package com.iapps.IappsReader.exceptions;

public class CustomException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
