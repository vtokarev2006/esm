package com.epam.esm.exceptions;

public class BadRequestException extends RuntimeException {
    private final ErrorCode errorCode;
    public BadRequestException(String message, ErrorCode errorCode)
    {
        super(message);
        this.errorCode = errorCode;
    }
    public String getErrorCode() {
        return errorCode.getCode();
    }

}
