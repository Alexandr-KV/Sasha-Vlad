package ru.otus.utils;

import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.exception.LoginException;
import ru.otus.exception.NoteNotFoundException;
import ru.otus.exception.RegistrationException;
import ru.otus.exception.ValidationException;

public class ExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    public static void handleLoginException(LoginException e, Context ctx) {
        logger.error("Возникло LoginException", e);
        ctx.json(e.getMessage());
    }

    public static void handleRegistrationException(RegistrationException e, Context ctx) {
        logger.error("Возникло RegistrationException", e);
        ctx.json(e.getMessage());
    }

    public static void handleNoteNotFoundException(NoteNotFoundException e, Context ctx) {
        logger.error("Возникло NoteNotFoundException", e);
        ctx.json(e.getMessage());
    }

    public static void handleValidException(ValidationException e, Context ctx) {
        logger.error("Возникло ValidException",e);
        ctx.json(e.getMessage());
    }
}
