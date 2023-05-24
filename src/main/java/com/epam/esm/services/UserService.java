package com.epam.esm.services;

import com.epam.esm.domain.User;
import com.epam.esm.hateoas.UserModel;
import com.epam.esm.hateoas.UserModelAssembler;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserModelAssembler userModelAssembler;

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public Page<User> findAllPageable(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public UserModel modelFromUser(User user, Class<?> controllerClass) {
        UserModel userModel = userModelAssembler.toModel(user);
        Link selfLink = linkTo(controllerClass).slash(user.getId()).withSelfRel();
        userModel.add(selfLink);
        return userModel;
    }
}