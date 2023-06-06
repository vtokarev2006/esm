package com.epam.esm.exceptions;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class UserAlreadyExistException extends AuthenticationException {
    private final ErrorCode errorCode;

    public UserAlreadyExistException(String message) {
        super(message);
        this.errorCode = ErrorCode.UserAlreadyExist;
    }
}