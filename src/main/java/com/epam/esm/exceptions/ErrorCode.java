package com.epam.esm.exceptions;

public enum ErrorCode {
    TagAlreadyExist("0010"),
    TagNotExist("0020"),
    CertificateNotExist("0030"),
    OrderNotExist("0040"),
    UserNotExist("0050"),
    NothingToUpdate("0060"),
    ObjectMalformed("0070"),
    UserNotExistInDtoObject("0080"),
    CertificateNotExistInDtoObject("0090"),
    UserAlreadyExist("0100"),
    BadCredentials("0110"),
    InsufficientAuthentication("0120"),

    AccessDenied("0130");

    private final String code;

    public String getCode() {
        return code;
    }

    ErrorCode(String code) {
        this.code = code;
    }
}