package com.epam.esm.controllers;

import com.epam.esm.domain.dto.AuthenticationRequestDto;
import com.epam.esm.domain.dto.AuthenticationResponseDto;
import com.epam.esm.domain.dto.RegisterUserDto;
import com.epam.esm.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponseDto register(@RequestBody RegisterUserDto registerUserDto) {
        return authenticationService.register(registerUserDto);
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponseDto authenticate(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
        return authenticationService.authenticate(authenticationRequestDto);
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        return "'as]peklrfiopwshb";
    }

}