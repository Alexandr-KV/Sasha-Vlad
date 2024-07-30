package ru.otus.request;

import ru.otus.ValidationException;

public class NoteRequest {
    private String title;
    private String message;

    public NoteRequest(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public NoteRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void validPostRequest() throws ValidationException {
        if (this.message == null || this.title == null) {
            throw new ValidationException("Отсутствует title и/или message");
        }
    }

    public void validPatchRequest() throws ValidationException {
        if (this.message == null && this.title == null) {
            throw new ValidationException("Отсутствует title и message");
        }
    }
}
