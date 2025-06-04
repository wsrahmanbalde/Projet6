package com.mdd.backend.Exceptions;

import org.springframework.http.HttpStatus;

public class SubjectNotFoundException extends ApiException {
    public SubjectNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}