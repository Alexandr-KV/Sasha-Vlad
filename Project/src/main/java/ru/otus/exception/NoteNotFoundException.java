package ru.otus.exception;

public class NoteNotFoundException extends RuntimeException {
    private final String message;
    public NoteNotFoundException(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
