package com.mdd.backend.Exceptions;

import org.springframework.http.HttpStatus;

public class AlreadySubscribedException extends ApiException {
    public AlreadySubscribedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}