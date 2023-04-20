package com.epam.esm.exceptions;

public class ResourceDoesNotExistException extends RuntimeException {
    public ResourceDoesNotExistException(String message) {
        super(message);
    }
}
