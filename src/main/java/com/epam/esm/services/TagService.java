package com.epam.esm.services;

import com.epam.esm.domain.Tag;
import com.epam.esm.domain.dto.TagOrdersPriceDto;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


//    final private TagRepository tagRepositoryJdbcTemplate;
    final private TagRepository tagRepositoryJpa;
    final private com.epam.esm.repository.springdata.TagRepository tagRepositorySpringData;

    public TagService(@Qualifier("TagRepositoryJpa") TagRepository tagRepositoryJpa, com.epam.esm.repository.springdata.TagRepository tagRepositorySpringData) {
        this.tagRepositoryJpa = tagRepositoryJpa;
        this.tagRepositorySpringData = tagRepositorySpringData;
    }

    public Tag get(long id) {
//        return tagRepositoryJdbcTemplate.get(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
        Optional<Tag> tag = tagRepositorySpringData.findById(id);
        if (tag.isEmpty())
            throw new EmptyResultDataAccessException(1);
        return tag.get();
    }

    public List<Tag> getAll() {
//        return tagRepositoryJdbcTemplate.getAll();
        return Streamable.of(tagRepositorySpringData.findAll(Sort.by(Sort.Direction.ASC, "id"))).toList();
    }

    public Page<Tag> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return tagRepositorySpringData.findAll(pageable);
    }

    public Tag create(Tag tag) {
//        return tagRepositoryJdbcTemplate.create(tag);
        return tagRepositorySpringData.save(tag);
    }

    public boolean delete(long id) {
//        return tagRepositoryJdbcTemplate.delete(id);
        if (tagRepositorySpringData.existsById(id)) {
            tagRepositorySpringData.deleteById(id);
            return true;
        } else
            return false;
    }

    public Tag getTagWithMaxSumOrdersPrice(long userId) {

        Optional<TagOrdersPriceDto> tagOrdersPriceDto = tagRepositoryJpa.getTagSumOrdersPrice(userId).stream()
                .peek(o -> logger.debug(o.toString()))
                .max(Comparator.comparingDouble(TagOrdersPriceDto::getPrice));


        if (tagOrdersPriceDto.isEmpty())
            throw new ResourceDoesNotExistException("No tags in orders for user, userId = " + userId);
        return tagOrdersPriceDto.get().getTag();
    }



}