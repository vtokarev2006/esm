package com.epam.esm.services;

import com.epam.esm.domain.User;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> get(long id) {
        return userRepository.get(id);
    }

    public List<User> getAll(){
        return userRepository.getAll();
    }


}
