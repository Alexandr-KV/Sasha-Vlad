package ru.otus;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import ru.otus.request.NotePatchRequest;
import ru.otus.request.NotePostRequest;
import ru.otus.response.NoteResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteController {
    private final NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void getAllNotes(Context ctx) throws SQLException, JsonProcessingException {
        List<Note> notes = noteRepository.readAllNotes();
        var responseArray = new ArrayList<NoteResponse>();
        for (var note : notes) {
            responseArray.add(new NoteResponse(note));
        }
        ctx.result(new ObjectMapper().writeValueAsString(responseArray));
    }

    public void getNote(Context ctx) throws SQLException, JsonProcessingException {
        Long id = Long.parseLong(ctx.pathParam("id"));
        NoteResponse noteResponse = new NoteResponse(noteRepository.readNoteById(id));
        ctx.result(new ObjectMapper().writeValueAsString(noteResponse));
    }

    public void postNote(Context ctx) throws SQLException, JsonProcessingException {
        NotePostRequest notePostRequest = ctx.bodyAsClass(NotePostRequest.class);
        notePostRequest.valid();
        Long id = noteRepository.writeNewNoteIntoRepository(notePostRequest.getTitle(), notePostRequest.getMessage());
        ctx.result(new ObjectMapper().writeValueAsString(id));
    }

    public void patchNote(Context ctx) throws SQLException {
        NotePatchRequest notePatchRequest = ctx.bodyAsClass(NotePatchRequest.class);
        notePatchRequest.valid();
        Long id = Long.parseLong(ctx.pathParam("id"));
        noteRepository.patchNoteById(id, notePatchRequest.getTitle(), notePatchRequest.getMessage());
    }

    public void deleteNote(Context ctx) throws SQLException {
        Long id = Long.parseLong(ctx.pathParam("id"));
        noteRepository.deleteNoteById(id);
    }
}
