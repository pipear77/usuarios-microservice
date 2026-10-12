package com.pragma.usuarios.jpa.helper;

import com.pragma.usuarios.model.role.Role;
import com.pragma.usuarios.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtProviderAdapterTest {

    @InjectMocks
    private JwtProviderAdapter jwtProviderAdapter;

    @BeforeEach
    void setUp() {
        // Inyectamos valores para las variables @Value de Spring
        ReflectionTestUtils.setField(jwtProviderAdapter, "secret", "super-secreta-clave-para-pruebas-unitarias-1234567890");
        ReflectionTestUtils.setField(jwtProviderAdapter, "expiration", 3600000);
    }

    @Test
    void generateToken_Success_ReturnsValidJwt() {
        User user = new User();
        user.setId(1L);
        user.setEmail("admin@test.com");
        user.setRole(new Role(1L, "ADMINISTRADOR", "Rol de admin"));

        String token = jwtProviderAdapter.generateToken(user);

        assertNotNull(token);
        assertEquals(3, token.split("\\.").length); // Un JWT válido tiene 3 partes separadas por puntos
    }
}