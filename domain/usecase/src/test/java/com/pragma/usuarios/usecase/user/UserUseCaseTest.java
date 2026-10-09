package com.pragma.usuarios.usecase.user;

import com.pragma.usuarios.model.user.User;
import com.pragma.usuarios.model.user.gateways.PasswordEncoderPort;
import com.pragma.usuarios.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private UserUseCase userUseCase;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = User.builder()
                .name("Juan")
                .lastName("Perez")
                .documentId("12345678")
                .phone("+573001234567")
                .birthDate(LocalDate.of(2000, 1, 1)) // 26 años de edad para el 2026
                .email("juan@test.com")
                .password("secreta")
                .build();
    }

    @Test
    void createUser_ValidUser_Success() {
        // Arrange
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(false);
        when(userRepository.existsByDocumentId(validUser.getDocumentId())).thenReturn(false);
        when(passwordEncoderPort.encode("secreta")).thenReturn("encriptada");
        when(userRepository.saveUser(any(User.class))).thenReturn(validUser);

        // Act
        User result = userUseCase.createUser(validUser);

        // Assert
        assertNotNull(result);
        verify(userRepository).saveUser(validUser);
    }

    @Test
    void createUser_Underage_ThrowsException() {
        // Arrange
        validUser.setBirthDate(LocalDate.now().minusYears(15)); // 15 años

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userUseCase.createUser(validUser);
        });
        assertEquals("El usuario debe ser mayor de edad.", exception.getMessage());
    }
}