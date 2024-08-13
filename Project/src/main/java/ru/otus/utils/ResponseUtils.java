package ru.otus.utils;

import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseUtils {
    private static final Logger logger = LoggerFactory.getLogger(ResponseUtils.class);

    public static void logResponseAfter(Context ctx) {
        logger.info("Окончен запрос: {} {}", ctx.method(), ctx.path());
    }

    public static void logResponseAfterMatched(Context ctx) {
        logger.info("Выдан ответ: {} {} {}", ctx.status(), ctx.headerMap(), ctx.result());
    }
}
