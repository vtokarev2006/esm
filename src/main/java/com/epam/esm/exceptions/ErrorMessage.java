package com.epam.esm.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {
    private String msg;
    private String errorCode;

}
