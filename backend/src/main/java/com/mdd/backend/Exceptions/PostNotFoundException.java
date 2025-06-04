package com.mdd.backend.Exceptions;

import org.springframework.http.HttpStatus;

public class PostNotFoundException extends ApiException {
    public PostNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
