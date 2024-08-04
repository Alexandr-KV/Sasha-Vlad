package ru.otus.response;

import ru.otus.entities.Note;

public class NoteResponse {
    private Long id;
    private String title;
    private String message;

    public NoteResponse(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.message = note.getMessage();
    }

    public NoteResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
