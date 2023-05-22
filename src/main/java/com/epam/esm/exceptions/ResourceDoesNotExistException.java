package com.epam.esm.exceptions;

public class ResourceDoesNotExistException extends RuntimeException {
    private final ErrorCode errorCode;

    public ResourceDoesNotExistException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode.getCode();
    }
}
