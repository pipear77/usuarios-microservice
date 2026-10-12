package com.pragma.usuarios.jpa.helper;

import com.pragma.usuarios.model.user.User;
import com.pragma.usuarios.model.user.gateways.JwtProviderPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtProviderAdapter implements JwtProviderPort {

    @Value("${jwt.secret:una-clave-secreta-muy-larga-y-segura-para-firmar-el-token-123456}")
    private String secret;

    @Value("${jwt.expiration:3600000}") // 1 hora por defecto
    private int expiration;

    @Override
    public String generateToken(User user) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        // Usamos la API moderna java.time como exige SonarQube
        Instant now = Instant.now();
        Instant expirationTime = now.plusMillis(expiration);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("role", "ROLE_" + user.getRole().getName())
                // Convertimos el Instant a Date solo para cumplir con la firma de JJWT
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}