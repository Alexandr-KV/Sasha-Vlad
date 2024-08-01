package ru.otus.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import ru.otus.entities.Note;
import ru.otus.ProjectRepository;
import ru.otus.request.NotePatchRequest;
import ru.otus.request.NotePostRequest;
import ru.otus.response.NoteResponse;
import ru.otus.utils.JwtUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteController {
    private final ProjectRepository projectRepository;
    private final JwtUtils jwtUtils;

    public NoteController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
        this.jwtUtils = new JwtUtils();
    }

    public void getAllNotes(Context ctx) throws SQLException, JsonProcessingException {
        jwtUtils.parse(ctx);
        List<Note> notes = projectRepository.readAllNotes();
        var responseArray = new ArrayList<NoteResponse>();
        for (var note : notes) {
            responseArray.add(new NoteResponse(note));
        }
        ctx.result(new ObjectMapper().writeValueAsString(responseArray));
    }

    public void getNoteById(Context ctx) throws SQLException, JsonProcessingException {
        jwtUtils.parse(ctx);
        Long id = Long.parseLong(ctx.pathParam("id"));
        NoteResponse noteResponse = new NoteResponse(projectRepository.readNoteById(id));
        ctx.result(new ObjectMapper().writeValueAsString(noteResponse));
    }

    public void postNote(Context ctx) throws SQLException, JsonProcessingException {
        NotePostRequest notePostRequest = ctx.bodyAsClass(NotePostRequest.class);
        notePostRequest.valid();
        Long id = projectRepository.writeNewNote(notePostRequest.getTitle(), notePostRequest.getMessage());
        ctx.result(new ObjectMapper().writeValueAsString(id));
    }

    public void patchNote(Context ctx) throws SQLException {
        NotePatchRequest notePatchRequest = ctx.bodyAsClass(NotePatchRequest.class);
        notePatchRequest.valid();
        Long id = Long.parseLong(ctx.pathParam("id"));
        projectRepository.patchNoteById(id, notePatchRequest.getTitle(), notePatchRequest.getMessage());
    }

    public void deleteNote(Context ctx) throws SQLException {
        Long id = Long.parseLong(ctx.pathParam("id"));
        projectRepository.deleteNoteById(id);
    }
}
