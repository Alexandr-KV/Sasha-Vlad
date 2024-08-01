package ru.otus;


import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.controller.NoteController;
import ru.otus.controller.UserController;
import ru.otus.exception.LoginException;
import ru.otus.exception.NoteNotFoundException;
import ru.otus.exception.RegistrationException;
import ru.otus.exception.ValidationException;
import ru.otus.utils.RequestUtils;

import java.sql.SQLException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SQLException {

        RequestUtils.info("Сервер запущен");

        ProjectRepository projectRepository = new ProjectRepository();
        NoteController noteController = new NoteController(projectRepository);
        UserController userController = new UserController(projectRepository);

        Javalin.create()
                .events(eventConfig -> eventConfig.serverStopping(projectRepository::closeDb))
                .before(ctx -> RequestUtils.info("Получен  запрос: " + ctx.method() + " " + ctx.path()))//из-за выноса логирования в отдельный класс
                //без конкатенации не обойтись
                .beforeMatched(ctx -> RequestUtils.info(ctx.headerMap() + " " + ctx.body()))
                .afterMatched(ctx -> RequestUtils.info("Выдан ответ: " + ctx.status() + " " + ctx.headerMap() + " " + ctx.result()))
                .after(ctx -> RequestUtils.info("Окончен запрос: " + ctx.method() + " " + ctx.path()))
                .exception(ValidationException.class, (e, ctx) -> RequestUtils.error("Возникло ValidException", "Возникло ValidException " + e, ctx))
                .exception(NoteNotFoundException.class, (e, ctx) -> RequestUtils.error("Возникло NoteNotFoundException", "Возникло NoteNotFoundException " + e, ctx))
                .exception(RegistrationException.class, (e, ctx) -> RequestUtils.error("Возникло RegistrationException", "Возникло RegistrationException " + e, ctx))
                .exception(LoginException.class, (e, ctx) -> RequestUtils.error("Возникло LoginException", "Возникло LoginException " + e, ctx))
                .get("/note", noteController::getAllNotes)
                .get("/note/{id}", noteController::getNoteById)
                .post("/note", noteController::postNote)
                .post("/registration", userController::registrationNewUser)
                .post("/login", userController::loginUser)
                .patch("/note/{id}", noteController::patchNote)
                .delete("/note/{id}", noteController::deleteNote)
                .start();
    }
}