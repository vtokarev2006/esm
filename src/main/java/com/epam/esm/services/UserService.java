package com.epam.esm.services;

import com.epam.esm.domain.User;
import com.epam.esm.hateoas.UserModel;
import com.epam.esm.hateoas.UserModelAssembler;
import com.epam.esm.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final com.epam.esm.repository.springdata.UserRepository userRepositorySpringData;
    private final UserModelAssembler userModelAssembler;


    public UserService(UserRepository userRepository, com.epam.esm.repository.springdata.UserRepository userRepositorySpringData, UserModelAssembler userModelAssembler) {
        this.userRepository = userRepository;
        this.userRepositorySpringData = userRepositorySpringData;
        this.userModelAssembler = userModelAssembler;
    }

    public Optional<User> get(long id) {
        return userRepository.get(id);
    }

    public Page<User> getAll(Pageable pageable) {
        return userRepositorySpringData.findAll(pageable);
    }

    public UserModel modelFromUser(User user, Class<?> controllerClass) {
        UserModel userModel = userModelAssembler.toModel(user);
        Link selfLink = linkTo(controllerClass).slash(user.getId()).withSelfRel();
        userModel.add(selfLink);
        return userModel;
    }
}
