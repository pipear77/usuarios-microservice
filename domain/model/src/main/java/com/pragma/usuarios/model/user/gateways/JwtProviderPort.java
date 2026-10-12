package com.pragma.usuarios.model.user.gateways;

import com.pragma.usuarios.model.user.User;

public interface JwtProviderPort {
    String generateToken(User user);
}