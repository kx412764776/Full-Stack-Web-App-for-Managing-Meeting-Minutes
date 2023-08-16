package com.apprenticeship.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class RequestException extends RuntimeException{
    public RequestException(String message) {
        super(message);
    }
}
