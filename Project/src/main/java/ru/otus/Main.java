package ru.otus;


import ru.otus.authentication.AuthService;
import ru.otus.exception.*;
import ru.otus.repository.NoteRepository;
import ru.otus.repository.RoleRepository;
import ru.otus.repository.UserRepository;
import io.javalin.Javalin;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.controller.NoteController;
import ru.otus.controller.UserController;
import ru.otus.exception.ExceptionHandler;
import ru.otus.utils.JwtUtils;
import ru.otus.utils.RequestUtils;
import ru.otus.utils.ResponseUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static ru.otus.authentication.Role.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SQLException {

        logger.info("Сервер запущен");

        Connection connection = DriverManager.getConnection("jdbc:sqlite:D:/ProjectDB.db");
        Statement statement = connection.createStatement();
        RoleRepository roleRepository = new RoleRepository(connection, statement);
        UserRepository userRepository = new UserRepository(connection, statement, roleRepository);
        NoteRepository noteRepository = new NoteRepository(connection, statement);
        JwtUtils jwtUtils = new JwtUtils(Jwts.SIG.HS256.key().build());
        NoteController noteController = new NoteController(noteRepository);
        UserController userController = new UserController(userRepository, jwtUtils);
        AuthService authService = new AuthService(userRepository, roleRepository, jwtUtils);

        Javalin.create()
                .events(eventConfig -> {
                    eventConfig.serverStopping(()->{
                        connection.close();
                        statement.close();
                    });
                })

                .before(RequestUtils::logRequestBefore)
                .beforeMatched(ctx -> {
                    RequestUtils.logRequestBeforeMatched(ctx);
                    authService.handleAccess(ctx);
                })
                .afterMatched(ResponseUtils::logResponseAfterMatched)
                .after(ResponseUtils::logResponseAfter)

                .exception(ValidationException.class, ExceptionHandler::handleValidException)
                .exception(NoteNotFoundException.class, ExceptionHandler::handleNoteNotFoundException)
                .exception(RegistrationException.class, ExceptionHandler::handleRegistrationException)
                .exception(LoginException.class, ExceptionHandler::handleLoginException)
                .exception(AuthException.class, ExceptionHandler::handleAuthException)

                .get("/note", noteController::getAllNotes, CLIENT, ADMIN)
                .get("/note/{id}", noteController::getNoteById, CLIENT, ADMIN)
                .post("/note", noteController::postNote, CLIENT, ADMIN)
                .patch("/note/{id}", noteController::patchNote, CLIENT, ADMIN)
                .delete("/note/{id}", noteController::deleteNote, CLIENT, ADMIN)

                .post("/registration", userController::registrationUser, NOT_REGISTERED)
                .post("/login", userController::loginUser, NOT_REGISTERED)

                .start();
    }
}