package com.pragma.usuarios.api.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final String secretKey = "my-super-secret-key-for-jwt-generation-pragma-2026-very-long";

    @BeforeEach
    void setUp() {
        // Inyectamos manualmente el @Value de Spring usando ReflectionTestUtils
        ReflectionTestUtils.setField(jwtAuthenticationFilter, "secretKey", secretKey);
        // Limpiamos el contexto antes de cada prueba por seguridad
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        // Limpiamos el contexto después para no afectar otros tests paralelos
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_NoAuthHeader_ContinuesChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication(), "El contexto debe estar nulo si no hay token");
    }

    @Test
    void doFilterInternal_InvalidAuthHeaderPrefix_ContinuesChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic un_token_cualquiera");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication(), "El contexto debe estar nulo si no es Bearer");
    }

    @Test
    void doFilterInternal_ValidToken_SetsAuthenticationContext() throws ServletException, IOException {
        // Generamos un token real y válido específicamente para esta prueba
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        String validToken = Jwts.builder()
                .subject("test@test.com")
                .claim("role", "ROLE_CLIENTE")
                .signWith(key)
                .compact();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication(), "El contexto no debe estar nulo con un token válido");
        assertEquals("test@test.com", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        assertEquals("ROLE_CLIENTE", SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void doFilterInternal_InvalidOrExpiredToken_ClearsContextAndContinues() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer un.token.totalmente_falso_o_corrupto");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication(), "El contexto debe limpiarse si el token falla");
    }
}