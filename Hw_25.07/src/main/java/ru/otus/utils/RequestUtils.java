package ru.otus.utils;

import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestUtils {
    private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    public static void logRequestBefore(Context ctx) {
        logger.info("Получен  запрос: {} {}", ctx.method(), ctx.path());
    }

    public static void logRequestBeforeMatched(Context ctx) {
        logger.info("{} {}", ctx.headerMap(), ctx.body());
    }
}
