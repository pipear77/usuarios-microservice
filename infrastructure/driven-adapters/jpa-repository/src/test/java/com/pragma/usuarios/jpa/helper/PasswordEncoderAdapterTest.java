package com.pragma.usuarios.jpa.helper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordEncoderAdapterTest {

    @Test
    void encode_ReturnsEncodedString() {
        PasswordEncoderAdapter adapter = new PasswordEncoderAdapter();
        String encoded = adapter.encode("miContraseñaSecreta");

        assertNotNull(encoded);
        // BCrypt siempre genera hashes que inician con $2a$ (o variaciones de $2)
        assertTrue(encoded.startsWith("$2a$"));
    }
}