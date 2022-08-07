package com.test.technical.controller.errorhandling.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super(String.format("An user already exists for this username : %s", username));
    }
}
