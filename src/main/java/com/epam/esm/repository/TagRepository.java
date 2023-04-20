package com.epam.esm.repository;

import com.epam.esm.domain.Tag;

import java.util.List;

public interface TagRepository extends GenericRepository<Tag> {
    Tag findByName(String name);
}
