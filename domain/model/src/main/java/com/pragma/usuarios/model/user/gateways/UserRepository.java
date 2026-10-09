package com.pragma.usuarios.model.user.gateways;

import com.pragma.usuarios.model.user.User;

public interface UserRepository {
    User saveUser(User user);
    boolean existsByEmail(String email);
    boolean existsByDocumentId(String documentId);
}