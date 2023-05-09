package com.epam.esm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

interface GenericRepository<T> {
    Optional<T> get(long id);
    List<T> getAll(Pageable pageable);
    T create(T t);
    void update(T t);
    boolean delete(long id);
}
