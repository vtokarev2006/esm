package com.epam.esm.controllers;

import com.epam.esm.domain.User;
import com.epam.esm.hateoas.UserModel;
import com.epam.esm.hateoas.UserModelAssembler;
import com.epam.esm.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserModelAssembler userModelAssembler;
    private final PagedResourcesAssembler<User> pagedUserResourcesAssembler;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserModel fetchById(@PathVariable long id) {
        return userService.modelFromUser(userService.findById(id), UserController.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<UserModel> fetchAllPageable(@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, value = 30) Pageable pageable) {
        return pagedUserResourcesAssembler.toModel(userService.findAllPageable(pageable), userModelAssembler);
    }
}