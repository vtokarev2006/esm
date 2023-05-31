package com.epam.esm.exceptions;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private final ErrorCode errorCode;
    public BadRequestException(String message, ErrorCode errorCode)
    {
        super(message);
        this.errorCode = errorCode;
    }
}
