package com.pragma.usuarios.api.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig securityConfig;

    @Mock
    private HttpSecurity httpSecurity;

    @Test
    @SuppressWarnings("unchecked")
    void securityFilterChainConfiguredSuccessfully() throws Exception {
        // Arrange: Interceptamos las llamadas a la API fluida para ejecutar las expresiones lambda
        when(httpSecurity.csrf(any())).thenAnswer(invocation -> {
            Customizer<CsrfConfigurer<HttpSecurity>> customizer = invocation.getArgument(0);
            customizer.customize(mock(CsrfConfigurer.class, RETURNS_DEEP_STUBS));
            return httpSecurity;
        });

        when(httpSecurity.sessionManagement(any())).thenAnswer(invocation -> {
            Customizer<SessionManagementConfigurer<HttpSecurity>> customizer = invocation.getArgument(0);
            customizer.customize(mock(SessionManagementConfigurer.class, RETURNS_DEEP_STUBS));
            return httpSecurity;
        });

        when(httpSecurity.authorizeHttpRequests(any())).thenAnswer(invocation -> {
            Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customizer = invocation.getArgument(0);
            customizer.customize(mock(AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry.class, RETURNS_DEEP_STUBS));
            return httpSecurity;
        });

        when(httpSecurity.build()).thenReturn(new DefaultSecurityFilterChain(null));

        // Act
        SecurityFilterChain chain = securityConfig.securityFilterChain(httpSecurity);

        // Assert
        assertNotNull(chain, "El SecurityFilterChain no debe ser nulo");
    }
}