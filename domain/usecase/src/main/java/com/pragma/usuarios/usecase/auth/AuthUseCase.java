package com.pragma.usuarios.usecase.user;

import com.pragma.usuarios.model.user.User;
import com.pragma.usuarios.model.user.gateways.JwtProviderPort;
import com.pragma.usuarios.model.user.gateways.PasswordEncoderPort;
import com.pragma.usuarios.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoderPort;
    private final JwtProviderPort jwtProviderPort;

    public String login(String email, String password) {
        User user = userRepository.getUserByEmail(email);

        if (user == null || !passwordEncoderPort.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        return jwtProviderPort.generateToken(user);
    }
}