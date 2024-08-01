package ru.otus.utils;

import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestUtils {
    private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);
    public static void error(String messageForUser, String messageForAdmin, Context ctx){
        logger.error(messageForAdmin);
        ctx.json(messageForUser);
    }

    public static void info(String message){
        logger.info(message);
    }
}
