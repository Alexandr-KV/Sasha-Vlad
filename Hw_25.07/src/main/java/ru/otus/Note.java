package ru.otus;

public class Note {
    private Long id;
    private String title;
    private String message;

    public Note(Long id, String title, String message) {
        this.id = id;
        this.title = title;
        this.message = message;
    }

    public Note(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public Note() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void validPostRequest() throws ValidationException {
        if (this.getMessage() == null || this.getTitle() == null) {
            throw new ValidationException("Ошибка в теле запроса");
        }
    }

    public void validPatchRequest() throws ValidationException {
        if (this.getMessage() == null && this.getTitle() == null) {
            throw new ValidationException("Ошибка в теле запроса");
        }
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
