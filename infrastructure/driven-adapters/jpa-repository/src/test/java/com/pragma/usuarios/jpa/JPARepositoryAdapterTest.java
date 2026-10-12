package com.pragma.usuarios.jpa;

import com.pragma.usuarios.jpa.entity.UserEntity;
import com.pragma.usuarios.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JPARepositoryAdapterTest {

    @Mock
    private JPARepository repository;

    @Mock
    private ObjectMapper mapper;

    private JPARepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new JPARepositoryAdapter(repository, mapper);
    }

    @Test
    void existsByEmail_ReturnsTrue() {
        when(repository.existsByEmail("test@test.com")).thenReturn(true);
        assertTrue(adapter.existsByEmail("test@test.com"));
    }

    @Test
    void existsByDocumentId_ReturnsTrue() {
        when(repository.existsByDocumentId("12345")).thenReturn(true);
        assertTrue(adapter.existsByDocumentId("12345"));
    }

    @Test
    void saveUser_ReturnsSavedUser() {
        User user = new User();
        UserEntity entity = new UserEntity();

        when(mapper.map(user, UserEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.map(entity, User.class)).thenReturn(user);

        User result = adapter.saveUser(user);
        assertEquals(user, result);
    }

    @Test
    void getUserByEmail_Found_ReturnsMappedUser() {
        // Arrange
        com.pragma.usuarios.jpa.entity.UserEntity entity = new com.pragma.usuarios.jpa.entity.UserEntity();
        entity.setEmail("admin@test.com");

        com.pragma.usuarios.model.user.User domainUser = new com.pragma.usuarios.model.user.User();
        domainUser.setEmail("admin@test.com");

        when(repository.findByEmail("admin@test.com")).thenReturn(entity);
        when(mapper.map(entity, com.pragma.usuarios.model.user.User.class)).thenReturn(domainUser);

        // Act
        com.pragma.usuarios.model.user.User result = adapter.getUserByEmail("admin@test.com");

        // Assert
        assertNotNull(result);
        assertEquals("admin@test.com", result.getEmail());
    }

    @Test
    void getUserByEmail_NotFound_ReturnsNull() {
        // Arrange
        when(repository.findByEmail("notfound@test.com")).thenReturn(null);

        // Act
        com.pragma.usuarios.model.user.User result = adapter.getUserByEmail("notfound@test.com");

        // Assert
        assertNull(result);
    }
}