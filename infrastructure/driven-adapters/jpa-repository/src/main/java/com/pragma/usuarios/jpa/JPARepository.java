package com.pragma.usuarios.jpa;

import com.pragma.usuarios.jpa.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

// Agregamos QueryByExampleExecutor para cumplir con el contrato del AdapterOperations
public interface JPARepository extends CrudRepository<UserEntity, Long>, QueryByExampleExecutor<UserEntity> {
    boolean existsByEmail(String email);
    boolean existsByDocumentId(String documentId);
    UserEntity findByEmail(String email);
}