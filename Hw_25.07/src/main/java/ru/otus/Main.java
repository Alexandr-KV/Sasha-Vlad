package ru.otus;


import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SQLException {
        logger.info("Сервер запущен");

        NoteRepository noteRepository = new NoteRepository();
        NoteController noteController = new NoteController(noteRepository);

        Javalin.create()
                .events(eventConfig -> eventConfig.serverStopping(noteRepository::closeDb))
                .before(ctx -> logger.info("Получен  запрос: {} {}", ctx.method(), ctx.path()))
                .beforeMatched(ctx -> logger.info(ctx.headerMap() + " " + ctx.body()))
                .after(ctx -> logger.info("Окончен запрос: {} {}", ctx.method(), ctx.path()))
                .afterMatched(ctx -> logger.info("Выдан ответ: {} {} {}", ctx.status(), ctx.headerMap(), ctx.result()))
                .exception(ValidationException.class, (e, ctx) -> {
                    logger.error("Возникло ValidException", e);
                    ctx.json(e.getMessage());
                })
                .get("/note", noteController::getAllNotes)
                .get("/note/{id}", noteController::getNote)
                .post("/note", noteController::postNote)
                .patch("/note/{id}", noteController::patchNote)
                .delete("/note/{id}", noteController::deleteNote)
                .start();
    }
}