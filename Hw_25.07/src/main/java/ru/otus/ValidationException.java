package ru.otus;

public class ValidationException extends RuntimeException{
    private String message;
    public ValidationException(String message) {
        this.message = message;
    }
}
