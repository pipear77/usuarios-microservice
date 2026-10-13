package com.pragma.usuarios.model.exception;

public class InvalidUserFieldException extends RuntimeException {
    public InvalidUserFieldException(String message) {
        super(message);
    }
}
