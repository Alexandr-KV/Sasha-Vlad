package ru.otus.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import ru.otus.entities.Note;
import ru.otus.repository.NoteRepository;
import ru.otus.request.NotePatchRequest;
import ru.otus.request.NotePostRequest;
import ru.otus.response.NoteResponse;
import ru.otus.utils.JwtUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteController {
    private final NoteRepository noteRepository;
    private final JwtUtils jwtUtils;

    public NoteController(NoteRepository noteRepository, JwtUtils jwtUtils) {
        this.noteRepository = noteRepository;
        this.jwtUtils = jwtUtils;
    }

    public void getAllNotes(Context ctx) throws SQLException, JsonProcessingException {
        var token = ctx.header("Authorization");
        jwtUtils.parse(token);
        List<Note> notes = noteRepository.readAllNotes();
        var responseArray = new ArrayList<NoteResponse>();
        for (var note : notes) {
            responseArray.add(new NoteResponse(note));
        }
        ctx.result(new ObjectMapper().writeValueAsString(responseArray));
    }

    public void getNoteById(Context ctx) throws SQLException, JsonProcessingException {
        var token = ctx.header("Authorization");
        jwtUtils.parse(token);
        Long id = Long.parseLong(ctx.pathParam("id"));
        NoteResponse noteResponse = new NoteResponse(noteRepository.readNoteById(id));
        ctx.result(new ObjectMapper().writeValueAsString(noteResponse));
    }

    public void postNote(Context ctx) throws SQLException, JsonProcessingException {
        var token = ctx.header("Authorization");
        jwtUtils.parse(token);
        NotePostRequest notePostRequest = ctx.bodyAsClass(NotePostRequest.class);
        notePostRequest.valid();
        Long id = noteRepository.writeNote(notePostRequest.getTitle(), notePostRequest.getMessage());
        ctx.result(new ObjectMapper().writeValueAsString(id));
    }

    public void patchNote(Context ctx) throws SQLException {
        var token = ctx.header("Authorization");
        jwtUtils.parse(token);
        NotePatchRequest notePatchRequest = ctx.bodyAsClass(NotePatchRequest.class);
        notePatchRequest.valid();
        Long id = Long.parseLong(ctx.pathParam("id"));
        noteRepository.patchNoteById(id, notePatchRequest.getTitle(), notePatchRequest.getMessage());
    }

    public void deleteNote(Context ctx) throws SQLException {
        var token = ctx.header("Authorization");
        jwtUtils.parse(token);
        Long id = Long.parseLong(ctx.pathParam("id"));
        noteRepository.deleteNoteById(id);
    }
}
