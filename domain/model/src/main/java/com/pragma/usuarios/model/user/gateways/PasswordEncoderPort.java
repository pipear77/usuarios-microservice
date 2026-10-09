package com.pragma.usuarios.model.user.gateways;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
}