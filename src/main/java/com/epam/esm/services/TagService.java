package com.epam.esm.services;

import com.epam.esm.controllers.TagController;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.dto.TagSummaryDto;
import com.epam.esm.exceptions.BadRequestException;
import com.epam.esm.exceptions.ErrorCode;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.exceptions.TagDuplicateNameException;
import com.epam.esm.hateoas.TagModel;
import com.epam.esm.hateoas.TagModelAssembler;
import com.epam.esm.hateoas.TagSummaryDtoModel;
import com.epam.esm.hateoas.TagSummaryDtoModelAssembler;
import com.epam.esm.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
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
        return tag.orElseThrow(() -> new ResourceDoesNotExistException("Tag not found, tagId=" + id, ErrorCode.TagNotExist));
    }

    public Page<Tag> findAllPageable(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    public Tag create(Tag tag) {
        tag.setId(null);
        try {
            return tagRepository.save(tag);
        } catch (DataIntegrityViolationException e) {
            if (tag.getName() == null || tag.getName().isEmpty()) {
                throw new BadRequestException("The name of the tag cannot be null or empty, object malformed", ErrorCode.ObjectMalformed);
            } else {
                throw new TagDuplicateNameException("Tag already exist, name = " + tag.getName());
            }
        }
    }

    public void delete(long id) {
        if (!tagRepository.existsById(id))
            throw new ResourceDoesNotExistException("Tag not found, tagId=" + id, ErrorCode.TagNotExist);
        tagRepository.deleteById(id);
    }

    @Deprecated
    public Optional<Tag> findMostWidelyUsedTagOfUserWithHighestCostOfOrders() {
        return tagRepository.findMostWidelyUsedTagOfUserWithHighestCostOfOrders();
    }

    public TagSummaryDto findTagSummaryByUserId(long userId) {
        try {
            return tagRepository
                    .findTagSummaryByUserId(userId).orElseThrow(() -> new InvalidDataAccessResourceUsageException("Summary tag of the requested user doesn't exist, userId = " + userId));
        } catch (InvalidDataAccessResourceUsageException e) {
            throw new ResourceDoesNotExistException("Summary tag of the requested user doesn't exist, userId = " + userId, ErrorCode.TagNotExist);
        }
    }

    public TagModel modelFromTag(Tag tag) {
        TagModel tagModel = tagModelAssembler.toModel(tag);
        Link selfLink = linkTo(TagController.class).slash(tag.getId()).withSelfRel();
        tagModel.add(selfLink);
        return tagModel;
    }

    public TagSummaryDtoModel modelFromTagSummaryDto(TagSummaryDto tagSummaryDto, long userId) {
        TagSummaryDtoModel tagSummaryDtoModel = tagSummaryDtoModelAssembler.toModel(tagSummaryDto);
        Link selfLink = linkTo(TagController.class)
                .slash("summary")
                .slash("users")
                .slash(userId)
                .withSelfRel();
        tagSummaryDtoModel.add(selfLink);
        return tagSummaryDtoModel;
    }
}