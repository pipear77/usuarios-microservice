package com.pragma.usuarios.api;

import com.pragma.usuarios.api.dto.UserRequestDto;
import com.pragma.usuarios.model.user.User;
import com.pragma.usuarios.usecase.user.UserUseCase;
import jakarta.validation.Valid;
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

        // NUEVA LÍNEA: Exponemos el id del restaurante
        response.put("idRestaurant", user.getIdRestaurant());

        // NUEVA LÍNEA: Exponemos el teléfono para el SMS
        response.put("phone", user.getPhone());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/employee")
    public ResponseEntity<Void> createEmployee(@Valid @RequestBody UserRequestDto request) {
        User user = new User();
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setDocumentId(request.getDocumentId());
        user.setPhone(request.getPhone());
        user.setBirthDate(request.getBirthDate());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        // NUEVA LÍNEA: Mapeamos el idRestaurante
        user.setIdRestaurant(request.getIdRestaurant());

        userUseCase.saveEmployee(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/client")
    public ResponseEntity<Void> createClient(@Valid @RequestBody UserRequestDto request) {
        User user = User.builder()
                .name(request.getName())
                .lastName(request.getLastName())
                .documentId(request.getDocumentId())
                .phone(request.getPhone())
                .birthDate(request.getBirthDate())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        userUseCase.createClient(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}