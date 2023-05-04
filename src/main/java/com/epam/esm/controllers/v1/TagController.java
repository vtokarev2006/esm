package com.epam.esm.controllers.v1;

import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.services.TagService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/tags")
public class TagController {

    final private TagService tagService;
    final private EntityManager em;

    @Autowired
    public TagController(TagService tagService, EntityManager em) {
        this.tagService = tagService;
        this.em = em;
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

    @GetMapping("/test")
    @Transactional
    public void test() {
        Tag tag1 = em.find(Tag.class, 14);
        tag1.setName(tag1.getName() + "upd-new-JPA");
        System.out.println("tag1 is in context = " + em.contains(tag1));
        Tag tag2 = Tag.builder().name("Tag1-new-JPA").build();
        em.persist(tag2);
        System.out.println("tag2 is in context = " + em.contains(tag2));
    }

    @GetMapping("/maxSumOrdersPrice")
    public Tag getTagWithMaxSumOrdersPrice(@RequestParam long userId) {
        return tagService.getTagWithMaxSumOrdersPrice(userId);
    }

}
