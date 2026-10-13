package com.pragma.usuarios.api.config;

// IMPORTANTE: Asegúrate de que el import de tu JwtAuthenticationFilter apunte al paquete correcto donde lo tengas creado.
import com.pragma.usuarios.api.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // Permite inyectar dependencias automáticamente a través del constructor
public class SecurityConfig {

    // Inyectamos tu filtro personalizado de JWT
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/v3/api-docs").permitAll()

                        // Nueva regla HU06: Solo el Propietario crea empleados
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/employee").hasRole("PROPIETARIO")

                        // Mantenemos la ruta pública temporal para poder seguir creando administradores/propietarios en pruebas
                        .requestMatchers("/api/v1/users/**").permitAll()

                        .anyRequest().authenticated()
                )
                // Habilitamos el filtro para interceptar el token de Swagger ANTES de validar la ruta
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}