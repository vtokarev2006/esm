package com.epam.esm.repository;

import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Optional;

@Deprecated
interface GenericRepository<T> {
    Optional<T> findById(long id);

    List<T> fetchAll();

    T create(T t);

    void update(T t);

    boolean delete(long id);
}
