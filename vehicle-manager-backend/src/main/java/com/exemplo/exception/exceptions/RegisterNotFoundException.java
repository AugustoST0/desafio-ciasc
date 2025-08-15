package com.exemplo.exception.exceptions;

public class RegisterNotFoundException extends RuntimeException{
    public RegisterNotFoundException(String message) {
        super(message);
    }
}
