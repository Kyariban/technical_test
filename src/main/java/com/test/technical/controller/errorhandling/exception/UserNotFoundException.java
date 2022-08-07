package com.test.technical.controller.errorhandling.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String username) {
        super(String.format("Cannot find an user with the specified username : %s", username));
    }
}
