package com.epam.esm.controllers;

import com.epam.esm.domain.User;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.hateoas.UserModel;
import com.epam.esm.hateoas.UserModelAssembler;
import com.epam.esm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/v2/users")
public class UserController {
    private final UserService userService;
    private final UserModelAssembler userModelAssembler;
    private final PagedResourcesAssembler<User> pagedUserResourcesAssembler;

    @Autowired
    public UserController(UserService userService, UserModelAssembler userModelAssembler, PagedResourcesAssembler<User> pagedResourcesAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
        this.pagedUserResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> fetchById(@PathVariable long id) {
        Optional<User> user = userService.get(id);
        if (user.isEmpty()) {
            throw new ResourceDoesNotExistException("User not found, id = " + id);
        }
        UserModel userModel = userService.modelFromUser(user.get(), UserController.class);
        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagedModel<UserModel>> fetchAllPageable(@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, value = 30) Pageable pageable) {
        Page<User> userPage = userService.getAll(pageable);
        PagedModel<UserModel> userModels = pagedUserResourcesAssembler.toModel(userPage, userModelAssembler);
        return new ResponseEntity<>(userModels, HttpStatus.OK);
    }
}