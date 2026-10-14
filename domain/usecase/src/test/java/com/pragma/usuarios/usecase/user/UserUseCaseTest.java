package com.pragma.usuarios.usecase.user;

import com.pragma.usuarios.model.exception.InvalidUserFieldException;
import com.pragma.usuarios.model.exception.UnderageUserException;
import com.pragma.usuarios.model.exception.UserAlreadyExistsException;
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
        assertEquals(2L, result.getRole().getId());
        verify(userRepository).saveUser(validUser);
    }

    @Test
    void saveEmployee_ValidUser_Success() {
        // Arrange
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(false);
        when(userRepository.existsByDocumentId(validUser.getDocumentId())).thenReturn(false);
        when(passwordEncoderPort.encode("secreta")).thenReturn("encriptada");

        // Act
        userUseCase.saveEmployee(validUser);

        // Assert
        assertEquals(3L, validUser.getRole().getId());
        verify(userRepository).saveUser(validUser);
    }

    @Test
    void createUser_Underage_ThrowsException() {
        // Arrange
        validUser.setBirthDate(LocalDate.now().minusYears(15)); // 15 años

        // Act & Assert
        UnderageUserException exception = assertThrows(UnderageUserException.class, () -> {
            userUseCase.createUser(validUser);
        });
        assertEquals("El usuario debe ser mayor de edad.", exception.getMessage());
    }

    @Test
    void createUser_EmailAlreadyExists_ThrowsException() {
        // Arrange
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(true);

        // Act & Assert
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userUseCase.createUser(validUser);
        });
        assertEquals("El correo ya se encuentra registrado.", exception.getMessage());
    }

    @Test
    void createUser_DocumentAlreadyExists_ThrowsException() {
        // Arrange
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(false);
        when(userRepository.existsByDocumentId(validUser.getDocumentId())).thenReturn(true);

        // Act & Assert
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userUseCase.createUser(validUser);
        });
        assertEquals("El documento de identidad ya se encuentra registrado.", exception.getMessage());
    }

    @Test
    void createUser_InvalidEmail_ThrowsException() {
        // Arrange
        validUser.setEmail("correo-invalido"); // Sin @

        // Act & Assert
        InvalidUserFieldException exception = assertThrows(InvalidUserFieldException.class, () -> {
            userUseCase.createUser(validUser);
        });
        assertEquals("El formato del correo es inválido.", exception.getMessage());
    }

    @Test
    void createUser_InvalidPhone_ThrowsException() {
        // Arrange
        validUser.setPhone("letras"); // Letras en lugar de números

        // Act & Assert
        InvalidUserFieldException exception = assertThrows(InvalidUserFieldException.class, () -> {
            userUseCase.createUser(validUser);
        });
        assertEquals("El celular debe ser numérico, máximo 13 caracteres y puede incluir '+'.", exception.getMessage());
    }

    @Test
    void createUser_InvalidDocumentId_ThrowsException() {
        // Arrange
        validUser.setDocumentId("1234ABC"); // Letras en el documento

        // Act & Assert
        InvalidUserFieldException exception = assertThrows(InvalidUserFieldException.class, () -> {
            userUseCase.createUser(validUser);
        });
        assertEquals("El documento de identidad debe ser únicamente numérico.", exception.getMessage());
    }

    @Test
    void createClient_ValidUser_Success() {
        // Arrange
        when(userRepository.existsByEmail(validUser.getEmail())).thenReturn(false);
        when(userRepository.existsByDocumentId(validUser.getDocumentId())).thenReturn(false);
        when(passwordEncoderPort.encode("secreta")).thenReturn("encriptada");

        // Act
        userUseCase.createClient(validUser);

        // Assert
        assertEquals(4L, validUser.getRole().getId());
        verify(userRepository).saveUser(validUser);
    }
}