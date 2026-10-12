package com.pragma.usuarios.usecase.auth;

import com.pragma.usuarios.model.user.User;
import com.pragma.usuarios.model.user.gateways.JwtProviderPort;
import com.pragma.usuarios.model.user.gateways.PasswordEncoderPort;
import com.pragma.usuarios.model.user.gateways.UserRepository;
import com.pragma.usuarios.usecase.user.AuthUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private JwtProviderPort jwtProviderPort;

    @InjectMocks
    private AuthUseCase authUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("admin@test.com");
        user.setPassword("hashed_password");
    }

    @Test
    void login_Success_ReturnsToken() {
        when(userRepository.getUserByEmail("admin@test.com")).thenReturn(user);
        when(passwordEncoderPort.matches("1234", "hashed_password")).thenReturn(true);
        when(jwtProviderPort.generateToken(user)).thenReturn("mocked-jwt-token");

        String token = authUseCase.login("admin@test.com", "1234");

        assertEquals("mocked-jwt-token", token);
        verify(userRepository).getUserByEmail("admin@test.com");
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        when(userRepository.getUserByEmail("notfound@test.com")).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authUseCase.login("notfound@test.com", "1234");
        });

        assertEquals("Credenciales inválidas", exception.getMessage());
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        when(userRepository.getUserByEmail("admin@test.com")).thenReturn(user);
        when(passwordEncoderPort.matches("wrong_pass", "hashed_password")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authUseCase.login("admin@test.com", "wrong_pass");
        });

        assertEquals("Credenciales inválidas", exception.getMessage());
    }
}