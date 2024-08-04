package ru.otus.authentication;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
    ADMIN,
    CLIENT,
    NOT_REGISTERED
}
