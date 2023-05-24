package com.epam.esm.services;

import com.epam.esm.controllers.TagController;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.dto.TagSummaryDto;
import com.epam.esm.hateoas.TagModel;
import com.epam.esm.hateoas.TagModelAssembler;
import com.epam.esm.hateoas.TagSummaryDtoModel;
import com.epam.esm.hateoas.TagSummaryDtoModelAssembler;
import com.epam.esm.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {
    final private TagRepository tagRepository;
    final private TagModelAssembler tagModelAssembler;
    final private TagSummaryDtoModelAssembler tagSummaryDtoModelAssembler;

    public Tag findById(long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return tag.get();
    }

    public Page<Tag> findAllPageable(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    public Tag create(Tag tag) {
        return tagRepository.save(tag);
    }

    public boolean delete(long id) {
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Deprecated
    public Optional<Tag> findMostWidelyUsedTagOfUserWithHighestCostOfOrders() {
        return tagRepository.findMostWidelyUsedTagOfUserWithHighestCostOfOrders();
    }

    public Optional<TagSummaryDto> findTagSummaryByUserId(long userId){
        return tagRepository.findTagSummaryByUserId(userId);
    }

    public TagModel modelFromTag(Tag tag) {
        TagModel tagModel = tagModelAssembler.toModel(tag);
        Link selfLink = linkTo(TagController.class).slash(tag.getId()).withSelfRel();
        tagModel.add(selfLink);
        return tagModel;
    }

    public TagSummaryDtoModel modelFromTagSummaryDto(TagSummaryDto tagSummaryDto, long userId){
        TagSummaryDtoModel tagSummaryDtoModel = tagSummaryDtoModelAssembler.toModel(tagSummaryDto);
        Link selfLink = linkTo(TagController.class)
                .slash("summary")
                .slash(userId)
                .withSelfRel();
        tagSummaryDtoModel.add(selfLink);
        return tagSummaryDtoModel;
    }
}