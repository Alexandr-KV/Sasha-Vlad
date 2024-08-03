package ru.otus;


import ru.otus.repository.NoteRepository;
import ru.otus.repository.UserRepository;
import io.javalin.Javalin;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.controller.NoteController;
import ru.otus.controller.UserController;
import ru.otus.exception.LoginException;
import ru.otus.exception.NoteNotFoundException;
import ru.otus.exception.RegistrationException;
import ru.otus.exception.ValidationException;
import ru.otus.utils.ExceptionHandler;
import ru.otus.utils.JwtUtils;
import ru.otus.utils.RequestUtils;
import ru.otus.utils.ResponseUtils;

import java.sql.SQLException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SQLException {

        logger.info("Сервер запущен");

        JwtUtils jwtUtils = new JwtUtils(Jwts.SIG.HS256.key().build());
        UserRepository userRepository = new UserRepository();
        NoteRepository noteRepository = new NoteRepository(userRepository.getConnection(), userRepository.getStatement());
        NoteController noteController = new NoteController(noteRepository, jwtUtils);
        UserController userController = new UserController(userRepository, jwtUtils);

        Javalin.create()
                .events(eventConfig -> {
                    eventConfig.serverStopping(noteRepository::closeNoteRepository);
                    eventConfig.serverStopping(userRepository::closeUserRepository);
                })
                .before(RequestUtils::logRequestBefore)
                .beforeMatched(RequestUtils::logRequestBeforeMatched)
                .afterMatched(ResponseUtils::logResponseAfterMatched)
                .after(ResponseUtils::logResponseAfter)
                .exception(ValidationException.class, ExceptionHandler::handleValidException)
                .exception(NoteNotFoundException.class, ExceptionHandler::handleNoteNotFoundException)
                .exception(RegistrationException.class, ExceptionHandler::handleRegistrationException)
                .exception(LoginException.class, ExceptionHandler::handleLoginException)
                .get("/note", noteController::getAllNotes)
                .get("/note/{id}", noteController::getNoteById)
                .post("/note", noteController::postNote)
                .post("/registration", userController::registrationUser)
                .post("/login", userController::loginUser)
                .patch("/note/{id}", noteController::patchNote)
                .delete("/note/{id}", noteController::deleteNote)
                .start();
    }
}