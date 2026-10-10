package com.pragma.usuarios.api;

import com.pragma.usuarios.api.dto.UserRequestDto;
import com.pragma.usuarios.model.role.Role;
import com.pragma.usuarios.model.user.User;
import com.pragma.usuarios.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserUseCase userUseCase;

    @InjectMocks
    private UserController userController;

    @Test
    void createOwner_ReturnsCreated() {
        UserRequestDto dto = new UserRequestDto("Juan", "Perez", "123", "300", LocalDate.of(2000, 1, 1), "j@j.com", "pass");
        ResponseEntity<String> response = userController.createOwner(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userUseCase).createUser(any(User.class));
    }

    @Test
    void getUserById_UserExists_ReturnsOk() {
        User user = new User();
        user.setId(1L);
        user.setRole(new Role(2L, "PROPIETARIO", "desc"));
        when(userUseCase.getUserById(1L)).thenReturn(user);

        ResponseEntity<Map<String, Object>> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().get("id"));
        assertEquals("PROPIETARIO", response.getBody().get("role"));
    }

    @Test
    void getUserById_UserNotFound_ReturnsNotFound() {
        when(userUseCase.getUserById(1L)).thenReturn(null);
        ResponseEntity<Map<String, Object>> response = userController.getUserById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}