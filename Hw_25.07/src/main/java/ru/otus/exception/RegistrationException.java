package ru.otus.exception;

public class RegistrationException extends RuntimeException{
    private final String message;

    public RegistrationException(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
