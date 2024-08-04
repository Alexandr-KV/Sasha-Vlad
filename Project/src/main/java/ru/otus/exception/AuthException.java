package ru.otus.exception;

public class AuthException extends RuntimeException{
    private final String message;

    public AuthException(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
