package com.epam.esm.legacy.repository;

import java.util.List;
import java.util.Optional;

@Deprecated
interface GenericRepository<T> {
    Optional<T> fetchById(long id);
    List<T> fetchAll();
    T create(T t);
    void update(T t);
    boolean delete(long id);
}