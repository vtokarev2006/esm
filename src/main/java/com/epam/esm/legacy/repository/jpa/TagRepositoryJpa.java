package com.epam.esm.legacy.repository.jpa;

import com.epam.esm.domain.Tag;
import com.epam.esm.domain.dto.TagOrdersPriceDto;
import com.epam.esm.legacy.repository.TagRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Deprecated
@Repository
@Profile("legacy")
@RequiredArgsConstructor
public class TagRepositoryJpa implements TagRepository {
    private final EntityManager em;

    @Override
    public Optional<Tag> fetchById(long id) {
        return Optional.ofNullable(em.find(Tag.class, id));
    }

    @Override
    public List<Tag> fetchAll() {
        throw new UnsupportedOperationException("public List<Tag> findAllPageable()");
    }

    @Override
    public Tag create(Tag tag) {
        em.persist(tag);
        return tag;
    }

    @Override
    public void update(Tag tag) {
        throw new UnsupportedOperationException("TagRepositoryJpa.update(Tag tag)");
    }

    @Override
    public boolean delete(long id) {
        throw new UnsupportedOperationException("TagRepositoryJpa.delete(long id)");
    }

    @Override
    public Tag fetchByName(String name) {
        throw new UnsupportedOperationException("TagRepositoryJpa.findByName(String name)");
    }

    public List<TagOrdersPriceDto> fetchTagSummaryByUserId(long userId) {
        return em.createNamedQuery("Tag.getTagSumOrdersPrice", TagOrdersPriceDto.class).setParameter("userId", userId).getResultList();
    }
}
