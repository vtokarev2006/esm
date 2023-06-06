package com.epam.esm.controllers;

import com.epam.esm.domain.Tag;
import com.epam.esm.hateoas.TagModel;
import com.epam.esm.hateoas.TagModelAssembler;
import com.epam.esm.hateoas.TagSummaryDtoModel;
import com.epam.esm.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagModelAssembler tagModelAssembler;
    private final PagedResourcesAssembler<Tag> pagedResourcesAssembler;
    final private TagService tagService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagModel fetchById(@PathVariable long id) {
        return tagService.modelFromTag(tagService.findById(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<TagModel> fetchAllPageable(@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, value = 30) Pageable pageable) {
        return pagedResourcesAssembler.toModel(tagService.findAllPageable(pageable), tagModelAssembler);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagModel create(@RequestBody Tag tag) {
        return tagService.modelFromTag(tagService.create(tag));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        tagService.delete(id);
    }

    @GetMapping("/summary/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public TagSummaryDtoModel fetchTagSummaryByUserId(@PathVariable long userId) {
        return tagService.modelFromTagSummaryDto(tagService.findTagSummaryByUserId(userId), userId);
    }
}