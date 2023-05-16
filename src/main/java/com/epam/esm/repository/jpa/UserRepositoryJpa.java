package com.epam.esm.repository.jpa;

import com.epam.esm.domain.User;
import com.epam.esm.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Deprecated
@Repository
@Profile("dev")
public class UserRepositoryJpa implements UserRepository {
    private final EntityManager em;

    @Autowired
    public UserRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public List<User> fetchAll() {
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
