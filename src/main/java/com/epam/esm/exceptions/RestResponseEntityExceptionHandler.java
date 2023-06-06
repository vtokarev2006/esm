package com.epam.esm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TagDuplicateNameException.class)
    public ResponseEntity<ErrorMessage> handleTagDuplicateNameException(TagDuplicateNameException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), e.getErrorCode().getCode()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), e.getErrorCode().getCode()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceDoesNotExistException.class)
    public ResponseEntity<ErrorMessage> handlerResourceDoesNotExistException(ResourceDoesNotExistException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), e.getErrorCode().getCode()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> handleUserAlreadyExistException(UserAlreadyExistException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), e.getErrorCode().getCode()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> handleBadCredentialsException(BadCredentialsException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ErrorCode.BadCredentials.getCode()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ErrorMessage> handleInsufficientAuthenticationException(InsufficientAuthenticationException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ErrorCode.InsufficientAuthentication.getCode()), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ErrorCode.AccessDenied.getCode()), HttpStatus.FORBIDDEN);
    }
}