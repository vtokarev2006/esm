package com.epam.esm.exceptions;

import lombok.Getter;

@Getter
public class ResourceDoesNotExistException extends RuntimeException {
    private final ErrorCode errorCode;

    public ResourceDoesNotExistException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
