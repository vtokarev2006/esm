package com.epam.esm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = TagDuplicateNameException.class)
    public ResponseEntity<ErrorMessage> handleTagDuplicateNameException(TagDuplicateNameException e) {
        return new ResponseEntity<>(ErrorMessage.builder().msg(e.getMessage()).errorCode(String.valueOf(HttpStatus.CONFLICT.value())).build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(ErrorMessage.builder().msg(e.getMessage()).errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value())).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ResourceDoesNotExistException.class)
    public ResponseEntity<ErrorMessage> handlerResourceDoesNotExistException(ResourceDoesNotExistException e) {
        return new ResponseEntity<>(ErrorMessage.builder().errorCode(String.valueOf(HttpStatus.NOT_FOUND.value())).msg(e.getMessage()).build(), HttpStatus.NOT_FOUND);
    }
}