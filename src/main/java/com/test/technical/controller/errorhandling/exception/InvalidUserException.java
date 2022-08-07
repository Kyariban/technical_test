package com.test.technical.controller.errorhandling.exception;


public class InvalidUserException extends RuntimeException {
    public InvalidUserException() {
        super("The given user is either not living in France and/or not an adult");
    }
}
