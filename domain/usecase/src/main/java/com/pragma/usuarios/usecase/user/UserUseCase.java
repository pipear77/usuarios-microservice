package com.pragma.usuarios.usecase.user;

import com.pragma.usuarios.model.exception.InvalidUserFieldException;
import com.pragma.usuarios.model.exception.UnderageUserException;
import com.pragma.usuarios.model.exception.UserAlreadyExistsException;
import com.pragma.usuarios.model.role.Role;
import com.pragma.usuarios.model.user.User;
import com.pragma.usuarios.model.user.gateways.PasswordEncoderPort;
import com.pragma.usuarios.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    public User createUser(User user) {
        validateUserRules(user);
        validateUserExistence(user);

        user.setPassword(passwordEncoderPort.encode(user.getPassword()));

        // Asignamos el rol real antes de persistir en BD
        Role ownerRole = Role.builder().id(2L).name("PROPIETARIO").build();
        user.setRole(ownerRole);

        return userRepository.saveUser(user);
    }

    public void saveEmployee(User user) {
        validateUserRules(user);
        validateUserExistence(user);

        // Se fuerza o valida que el rol sea EMPLEADO (ID 3) cumpliendo el criterio 3
        Role role = new Role();
        role.setId(3L);
        user.setRole(role);

        // Encriptación obligatoria con BCrypt
        user.setPassword(passwordEncoderPort.encode(user.getPassword()));

        userRepository.saveUser(user);
    }

    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    private void validateUserRules(User user) {
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidUserFieldException("El formato del correo es inválido.");
        }
        if (!user.getPhone().matches("^\\+?\\d{1,12}$")) {
            throw new InvalidUserFieldException("El celular debe ser numérico, máximo 13 caracteres y puede incluir '+'.");
        }
        if (!user.getDocumentId().matches("^\\d+$")) {
            throw new InvalidUserFieldException("El documento de identidad debe ser únicamente numérico.");
        }
        if (!user.isAdult()) {
            throw new UnderageUserException("El usuario debe ser mayor de edad.");
        }
    }

    private void validateUserExistence(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("El correo ya se encuentra registrado.");
        }
        if (userRepository.existsByDocumentId(user.getDocumentId())) {
            throw new UserAlreadyExistsException("El documento de identidad ya se encuentra registrado.");
        }
    }

    public void createClient(User user) {
        validateUserRules(user);
        validateUserExistence(user);

        // Criterio 3: El usuario quedará con el rol cliente
        Role clientRole = new Role();
        clientRole.setId(4L);
        clientRole.setName("CLIENTE");
        user.setRole(clientRole);

        // Criterio 1: Clave encriptada con bcrypt[cite: 14]
        user.setPassword(passwordEncoderPort.encode(user.getPassword()));

        userRepository.saveUser(user);
    }
}