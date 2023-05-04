package com.epam.esm.repository.jpa;

import com.epam.esm.domain.User;
import com.epam.esm.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("UserRepositoryJpa")
public class UserRepositoryJpa implements UserRepository {

    private final EntityManager em;

    @Autowired
    public UserRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public List<User> getAll() {
        return em.createNamedQuery("get all users", User.class).getResultList();
    }

    @Override
    public User create(User user) {
        throw new UnsupportedOperationException("create");
    }

    @Override
    public void update(User user) {
        throw new UnsupportedOperationException("update");
    }

    @Override
    public boolean delete(long id) {
        throw new UnsupportedOperationException("delete");
    }
}
