package com.epam.esm.controllers.v2;

import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.exceptions.TagDuplicateNameException;
import com.epam.esm.hateoas.TagModel;
import com.epam.esm.hateoas.TagModelAssembler;
import com.epam.esm.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController("TagControllerV2")
@RequestMapping("api/v2/tags")
public class TagController {

    private final TagModelAssembler tagModelAssembler;
    private final PagedResourcesAssembler<Tag> pagedResourcesAssembler;
    final private TagService tagService;

    @Autowired
    public TagController(TagModelAssembler tagModelAssembler, PagedResourcesAssembler<Tag> pagedResourcesAssembler, TagService tagService) {
        this.tagModelAssembler = tagModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.tagService = tagService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagModel> getById(@PathVariable long id) {
        try {
            Tag tag = tagService.get(id);
            return new ResponseEntity<>(tagService.modelFromTag(tag), HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceDoesNotExistException("Tag not found, tagId=" + id);
        }
    }

    @GetMapping
    public ResponseEntity<PagedModel<TagModel>> getAll(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, value = 30) Pageable pageable) {
        Page<Tag> tagPage = tagService.getAll(pageable);
        PagedModel<TagModel> tagModel = pagedResourcesAssembler.toModel(tagPage, tagModelAssembler);
        return new ResponseEntity<>(tagModel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TagModel> create(@RequestBody Tag tag) {
        try {
            Tag newTag = tagService.create(tag);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newTag.getId())
                    .toUri();

            TagModel tagModel = tagModelAssembler.toModel(newTag);
            tagModel.add(Link.of(String.valueOf(location)));
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header(HttpHeaders.LOCATION, String.valueOf(location))
                    .body(tagModel);
        } catch (DataIntegrityViolationException e) {
            throw new TagDuplicateNameException("Tag already exist, name = " + tag.getName());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        if (!tagService.delete(id)) {
            throw new ResourceDoesNotExistException("Tag not found, tagId=" + id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/mostWidelyUsedTagOfUserWithHighestCostOfOrders")
    public ResponseEntity<TagModel> mostWidelyUsedTagOfUserWithHighestCostOfOrders() {
        Tag tag = tagService.mostWidelyUsedTagOfUserWithHighestCostOfOrders();
        return new ResponseEntity<>(tagService.modelFromTag(tag), HttpStatus.OK);
    }
}