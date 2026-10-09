package com.pragma.usuarios.usecase.user;

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

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("El correo ya se encuentra registrado.");
        }
        if (userRepository.existsByDocumentId(user.getDocumentId())) {
            throw new IllegalArgumentException("El documento de identidad ya se encuentra registrado.");
        }

        user.setPassword(passwordEncoderPort.encode(user.getPassword()));
        return userRepository.saveUser(user);
    }

    private void validateUserRules(User user) {
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El formato del correo es inválido.");
        }
        if (!user.getPhone().matches("^\\+?\\d{1,12}$")) {
            throw new IllegalArgumentException("El celular debe ser numérico, máximo 13 caracteres y puede incluir '+'.");
        }
        if (!user.getDocumentId().matches("^\\d+$")) {
            throw new IllegalArgumentException("El documento de identidad debe ser únicamente numérico.");
        }
        if (!user.isAdult()) {
            throw new IllegalArgumentException("El usuario debe ser mayor de edad.");
        }
    }
}