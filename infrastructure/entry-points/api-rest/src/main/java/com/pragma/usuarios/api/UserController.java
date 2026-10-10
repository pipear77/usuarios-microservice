package com.pragma.usuarios.api;

import com.pragma.usuarios.api.dto.UserRequestDto;
import com.pragma.usuarios.model.user.User;
import com.pragma.usuarios.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @PostMapping("/owner")
    public ResponseEntity<String> createOwner(@RequestBody UserRequestDto request) {
        // Mapeo manual rápido de DTO a Dominio
        User user = User.builder()
                .name(request.getName())
                .lastName(request.getLastName())
                .documentId(request.getDocumentId())
                .phone(request.getPhone())
                .birthDate(request.getBirthDate())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        userUseCase.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable("id") Long id) {
        User user = userUseCase.getUserById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        // Lectura real de la base de datos
        response.put("role", user.getRole().getName());

        return ResponseEntity.ok(response);
    }
}