package ru.otus.utils;

import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.exception.*;

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

    public static void handleAuthException(AuthException e, Context ctx){
        logger.error("Возникло AuthException",e);
        ctx.json(e.getMessage());
    }
}
