package com.pragma.usuarios.jpa;

import com.pragma.usuarios.jpa.entity.UserEntity;
import com.pragma.usuarios.jpa.helper.AdapterOperations;
import com.pragma.usuarios.model.user.User;
import com.pragma.usuarios.model.user.gateways.UserRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JPARepositoryAdapter extends AdapterOperations<User, UserEntity, Long, JPARepository>
        implements UserRepository {

    public JPARepositoryAdapter(JPARepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
    }

    @Override
    public User saveUser(User user) {
        return super.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByDocumentId(String documentId) {
        return repository.existsByDocumentId(documentId);
    }

    @Override
    public User getUserById(Long id) {
        // super.findById() ya busca en BD y mapea automáticamente a tu objeto de Dominio (User)
        return super.findById(id);
    }
}