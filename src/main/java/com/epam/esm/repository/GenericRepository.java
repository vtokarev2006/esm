package com.epam.esm.repository;

import java.util.List;
import java.util.Optional;

interface GenericRepository<T> {
    Optional<T> get(long id);
    List<T> getAll();
    T create(T t);
    void update(T t);
    boolean delete(long id);
}
