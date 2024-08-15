package ru.otus.request;

import ru.otus.exception.ValidationException;

public class NotePostRequest {
    private String title;
    private String message;


    public NotePostRequest() {
    }

    public String getTitle() {
        return title;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void valid() throws ValidationException {
        if (this.message == null || this.title == null) {
            throw new ValidationException("Отсутствует title и/или message");
        }
    }

}
