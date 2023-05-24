package com.epam.esm.controllers;

import com.epam.esm.domain.Tag;
import com.epam.esm.domain.dto.TagSummaryDto;
import com.epam.esm.exceptions.ErrorCode;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.exceptions.TagDuplicateNameException;
import com.epam.esm.hateoas.TagModel;
import com.epam.esm.hateoas.TagModelAssembler;
import com.epam.esm.hateoas.TagSummaryDtoModel;
import com.epam.esm.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v2/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagModelAssembler tagModelAssembler;
    private final PagedResourcesAssembler<Tag> pagedResourcesAssembler;
    final private TagService tagService;

    @GetMapping("/{id}")
    public ResponseEntity<TagModel> fetchById(@PathVariable long id) {
        try {
            Tag tag = tagService.findById(id);
            return new ResponseEntity<>(tagService.modelFromTag(tag), HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceDoesNotExistException("Tag not found, tagId=" + id, ErrorCode.TagNotExist);
        }
    }

    @GetMapping
    public ResponseEntity<PagedModel<TagModel>> fetchAllPageable(@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, value = 30) Pageable pageable) {
        Page<Tag> tagPage = tagService.findAllPageable(pageable);
        PagedModel<TagModel> tagModel = pagedResourcesAssembler.toModel(tagPage, tagModelAssembler);
        return new ResponseEntity<>(tagModel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TagModel> create(@RequestBody Tag tag) {
        try {
            Tag newTag = tagService.create(tag);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newTag.getId()).toUri();
            TagModel tagModel = tagModelAssembler.toModel(newTag);
            tagModel.add(Link.of(String.valueOf(location)));
            return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, String.valueOf(location)).body(tagModel);
        } catch (DataIntegrityViolationException e) {
            throw new TagDuplicateNameException("Tag already exist, name = " + tag.getName());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        if (!tagService.delete(id)) {
            throw new ResourceDoesNotExistException("Tag not found, tagId=" + id, ErrorCode.TagNotExist);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/summary/{userId}")
    public ResponseEntity<TagSummaryDtoModel> fetchTagSummaryByUserId(@PathVariable long userId) {
        TagSummaryDto tagSummaryDto = tagService.findTagSummaryByUserId(userId).orElseThrow(() -> new ResourceDoesNotExistException("The most widely used tag of user with the highest cost of orders doesn't exist", ErrorCode.TagNotExist));
        return new ResponseEntity<>(tagService.modelFromTagSummaryDto(tagSummaryDto, userId), HttpStatus.OK);
    }
}