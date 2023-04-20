package com.epam.esm.controllers;

import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.BadRequestException;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.services.TagService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/tags")
public class TagController {

    final private TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/{id}")
    public Tag getById(@PathVariable long id) {
        try {
            return tagService.get(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceDoesNotExistException("Tag not found, tagId=" + id);
        }
    }

    @GetMapping
    public List<Tag> getAll() {
        return tagService.getAll();
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Tag tag) {
        Tag newTag = tagService.create(tag);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newTag.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, String.valueOf(location)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        if (!tagService.delete(id)) {
            throw new ResourceDoesNotExistException("Tag not found, tagId=" + id);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
