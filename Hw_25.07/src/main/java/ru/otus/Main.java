package ru.otus;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Сервер запущен");
        Javalin.create()
                .before(ctx -> {
                    logger.info("Получен  запрос: {} {}", ctx.method(), ctx.path());
                    DbRepository.Connect();
                })
                .beforeMatched(ctx -> logger.info(ctx.headerMap() + " " + ctx.body()))
                .after(ctx -> {
                    logger.info("Окончен запрос: {} {}", ctx.method(), ctx.path());
                    DbRepository.CloseDB();
                })
                .afterMatched(ctx -> logger.info("Выдан ответ: " + ctx.status() + " " + ctx.headerMap() + " " + ctx.result()))
                .exception(ValidationException.class, (e, ctx) -> {
                    logger.error("Возникло ValidException" + e.getMessage());
                    ctx.json(e.getMessage());
                })
                .get("/note", ctx -> {
                    List<Note> notes = DbRepository.ReadAllDB();
                    ctx.result(new ObjectMapper().writeValueAsString(notes));
                })
                .get("/note/{id}", ctx -> {
                    Long id = Long.parseLong(ctx.pathParam("id"));
                    Note note = DbRepository.ReadDBbyId(id);
                    ctx.result(new ObjectMapper().writeValueAsString(note));
                })
                .post("/note", ctx -> {
                    Note note = ctx.bodyAsClass(Note.class);
                    note.validPostRequest();
                    Long id = DbRepository.WriteNewNoteIntoDB(note.getTitle(), note.getMessage());
                    ctx.result(new ObjectMapper().writeValueAsString(id));
                })
                .patch("/note/{id}", ctx -> {
                    Note note = ctx.bodyAsClass(Note.class);
                    note.validPatchRequest();
                    Long id = Long.parseLong(ctx.pathParam("id"));
                    DbRepository.patchNoteById(id, note);
                })
                .delete("/note/{id}", ctx -> {
                    Long id = Long.parseLong(ctx.pathParam("id"));
                    DbRepository.deleteById(id);
                })
                .start();
    }
}