package com.epam.esm.exceptions;

public class TagDuplicateNameException extends RuntimeException {
    private final ErrorCode errorCode;

    public TagDuplicateNameException(String message) {
        super(message);
        this.errorCode = ErrorCode.TagAlreadyExist;
    }

    public String getErrorCode() {
        return errorCode.getCode();
    }
}
