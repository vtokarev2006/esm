package com.epam.esm.services;

import com.epam.esm.domain.Role;
import com.epam.esm.domain.User;
import com.epam.esm.domain.dto.AuthenticationRequestDto;
import com.epam.esm.domain.dto.AuthenticationResponseDto;
import com.epam.esm.domain.dto.RegisterUserDto;
import com.epam.esm.exceptions.BadRequestException;
import com.epam.esm.exceptions.ErrorCode;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.exceptions.UserAlreadyExistException;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequestDto.getEmail(), authenticationRequestDto.getPassword()));
        User user = userRepository.findByEmail(authenticationRequestDto.getEmail())
                .orElseThrow(() -> new ResourceDoesNotExistException(format("User with email: %s, not found", authenticationRequestDto.getEmail()), ErrorCode.UserNotExist));
        String token = jwtService.generateToken(user);
        return new AuthenticationResponseDto(token);
    }

    public AuthenticationResponseDto register(RegisterUserDto registerUserDto) {
        if(Strings.isEmpty(registerUserDto.getEmail()) || Strings.isEmpty(registerUserDto.getPassword())) {
            throw new BadRequestException("User register request malformed, email or password is empty", ErrorCode.ObjectMalformed);
        }
        if (userRepository.findByEmail(registerUserDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException(format("User with email: %s, already exist", registerUserDto.getEmail()));
        }
        User user = User.builder()
                .email(registerUserDto.getEmail())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .role(Role.USER)
                .build();
        user = userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthenticationResponseDto(token);
    }
}