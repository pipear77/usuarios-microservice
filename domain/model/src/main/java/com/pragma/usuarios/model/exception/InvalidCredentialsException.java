package com.pragma.usuarios.model.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() { super("Credenciales inválidas"); }
}
