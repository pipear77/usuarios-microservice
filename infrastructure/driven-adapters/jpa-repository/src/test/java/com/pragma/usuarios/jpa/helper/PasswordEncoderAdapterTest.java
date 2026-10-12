package com.pragma.usuarios.jpa.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderAdapterTest {

    private final PasswordEncoderAdapter adapter = new PasswordEncoderAdapter();

    @Test
    void encode_ReturnsEncodedString() {
        PasswordEncoderAdapter adapter = new PasswordEncoderAdapter();
        String encoded = adapter.encode("miContraseñaSecreta");

        assertNotNull(encoded);
        // BCrypt siempre genera hashes que inician con $2a$ (o variaciones de $2)
        assertTrue(encoded.startsWith("$2a$"));
    }

    @Test
    void encodeAndMatches_Success() {
        String raw = "miPasswordSecreto";
        String encoded = adapter.encode(raw);

        assertNotNull(encoded);
        assertTrue(adapter.matches(raw, encoded));
        assertFalse(adapter.matches("passwordIncorrecto", encoded));
    }
}