package com.epam.esm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = TagDuplicateNameException.class)
    public ResponseEntity<String> handleTagDuplicateNameException(TagDuplicateNameException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ResourceDoesNotExistException.class)
    public ResponseEntity<ErrorMessage> handlerResourceDoesNotExistException(ResourceDoesNotExistException e) {
        return new ResponseEntity<>(ErrorMessage.builder().errorCode(HttpStatus.NOT_FOUND.name()).msg(e.getMessage()).build(), HttpStatus.NOT_FOUND);
    }



}