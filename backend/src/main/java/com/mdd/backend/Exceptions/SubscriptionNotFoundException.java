package com.mdd.backend.Exceptions;

import org.springframework.http.HttpStatus;

public class SubscriptionNotFoundException extends ApiException {
    public SubscriptionNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}