package ru.otus.exception;

public class LoginException extends RuntimeException{
    private final String message;

    public LoginException(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
