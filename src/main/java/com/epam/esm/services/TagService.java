package com.epam.esm.services;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    final private TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag get(long id) {
        return tagRepository.get(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    public List<Tag> getAll() {
        return tagRepository.getAll();
    }

    public Tag create(Tag tag) {
        return tagRepository.create(tag);
    }

    public boolean delete(long id) {
        return tagRepository.delete(id);
    }
}
