package com.mdd.backend.Models.Auth;

public record RegisterRequest(
        String username,
        String email,
        String password
) {}
