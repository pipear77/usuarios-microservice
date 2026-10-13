package com.pragma.usuarios.api.exception;

import com.pragma.usuarios.model.exception.InvalidCredentialsException;
import com.pragma.usuarios.model.exception.InvalidUserFieldException;
import com.pragma.usuarios.model.exception.UnderageUserException;
import com.pragma.usuarios.model.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleUserAlreadyExists_ReturnsConflict() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException("El correo ya existe");
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleUserAlreadyExists(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("El correo ya existe", response.getBody().get("error"));
    }

    @Test
    void handleValidationExceptions_ReturnsBadRequest() {
        InvalidUserFieldException ex = new InvalidUserFieldException("Formato inválido");
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Formato inválido", response.getBody().get("error"));
    }

    @Test
    void handleUnderageUser_ReturnsBadRequest() {
        UnderageUserException ex = new UnderageUserException("Debe ser mayor de edad");
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Debe ser mayor de edad", response.getBody().get("error"));
    }

    @Test
    void handleInvalidCredentials_ReturnsUnauthorized() {
        InvalidCredentialsException ex = new InvalidCredentialsException();
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleInvalidCredentials(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciales inválidas", response.getBody().get("error"));
    }
}