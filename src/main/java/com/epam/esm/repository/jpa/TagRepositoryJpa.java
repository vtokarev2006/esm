package com.epam.esm.repository.jpa;

import com.epam.esm.domain.Tag;
import com.epam.esm.domain.dto.TagOrdersPriceDto;
import com.epam.esm.repository.TagRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("TagRepositoryJpa")
public class TagRepositoryJpa implements TagRepository {

    private final EntityManager em;

    @Autowired
    public TagRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Tag> get(long id) {
        return Optional.ofNullable(em.find(Tag.class, id));
    }

    @Override
    public List<Tag> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public Tag create(Tag tag) {
        em.persist(tag);
        return tag;
    }

    @Override
    public void update(Tag tag) {
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public Tag findByName(String name) {
        return null;
    }

    public List<TagOrdersPriceDto> getTagSumOrdersPrice(long userId){
        return em.createNamedQuery("Tag_getTagSumOrdersPrice", TagOrdersPriceDto.class).setParameter("userId", userId).getResultList();
    }


}
