package ru.otus.request;

import ru.otus.exception.ValidationException;

public class NotePatchRequest {
    private String title;
    private String message;

    public NotePatchRequest(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public NotePatchRequest() {
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

    public void valid() throws ValidationException {
        if (this.message == null && this.title == null) {
            throw new ValidationException("Отсутствует title и message");
        }
    }
}
