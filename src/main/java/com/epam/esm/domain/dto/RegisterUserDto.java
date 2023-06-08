package com.epam.esm.domain.dto;

import lombok.Data;

@Data
public class RegisterUserDto {
    private String email;
    private String password;
}
