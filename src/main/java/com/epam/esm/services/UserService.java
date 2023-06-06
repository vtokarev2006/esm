package com.epam.esm.services;

import com.epam.esm.domain.User;
import com.epam.esm.exceptions.ErrorCode;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.hateoas.UserModel;
import com.epam.esm.hateoas.UserModelAssembler;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserModelAssembler userModelAssembler;


    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("User not found, id = " + id, ErrorCode.UserNotExist));
    }

    public Page<User> findAllPageable(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(format("User: %s, not found", username)));
    }
    public UserModel modelFromUser(User user, Class<?> controllerClass) {
        UserModel userModel = userModelAssembler.toModel(user);
        Link selfLink = linkTo(controllerClass).slash(user.getId()).withSelfRel();
        userModel.add(selfLink);
        return userModel;
    }
}