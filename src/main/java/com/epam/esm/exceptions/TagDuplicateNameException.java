package com.epam.esm.exceptions;

import lombok.Getter;

@Getter
public class TagDuplicateNameException extends RuntimeException {
    private final ErrorCode errorCode;

    public TagDuplicateNameException(String message) {
        super(message);
        this.errorCode = ErrorCode.TagAlreadyExist;
    }
}
