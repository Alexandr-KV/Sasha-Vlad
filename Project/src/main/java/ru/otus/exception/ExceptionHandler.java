package ru.otus.exception;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    public static void handleLoginException(LoginException e, Context ctx) {
        logger.error("Возникло LoginException", e);
        ctx.status(HttpStatus.FORBIDDEN);
        ctx.json(e.getMessage());
    }

    public static void handleRegistrationException(RegistrationException e, Context ctx) {
        logger.error("Возникло RegistrationException", e);
        ctx.status(HttpStatus.FORBIDDEN);
        ctx.json(e.getMessage());
    }

    public static void handleNoteNotFoundException(NoteNotFoundException e, Context ctx) {
        logger.error("Возникло NoteNotFoundException", e);
        ctx.status(HttpStatus.NOT_FOUND);
        ctx.json(e.getMessage());
    }

    public static void handleValidException(ValidationException e, Context ctx) {
        logger.error("Возникло ValidException",e);
        ctx.status(HttpStatus.BAD_REQUEST);
        ctx.json(e.getMessage());
    }

    public static void handleAuthException(AuthException e, Context ctx){
        logger.error("Возникло AuthException",e);
        ctx.status(HttpStatus.UNAUTHORIZED);
        ctx.json(e.getMessage());
    }

    public static void handleException(Exception e, Context ctx){
        logger.error("Возникло необычное исключение: ", e);
        ctx.status(HttpStatus.FORBIDDEN);
        ctx.json("Необычная ошибка, обратитесь в поддержку");
    }
}
