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
        // El ObjectMapper convierte automáticamente entre User (Dominio) y UserEntity (BD)
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public User saveUser(User user) {
        // super.save() ya viene incluido en AdapterOperations y hace la conversión por debajo
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
}
