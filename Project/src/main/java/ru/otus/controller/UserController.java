package ru.otus.controller;

import ru.otus.repository.UserRepository;
import io.javalin.http.Context;
import ru.otus.entities.User;
import ru.otus.exception.LoginException;
import ru.otus.exception.RegistrationException;
import ru.otus.request.LoginRequest;
import ru.otus.request.RegistrationRequest;
import ru.otus.utils.JwtUtils;

import java.sql.SQLException;
import java.util.Objects;

public class UserController {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public UserController(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public void registrationUser(Context ctx) throws SQLException, RegistrationException {
        RegistrationRequest registrationRequest = ctx.bodyAsClass(RegistrationRequest.class);
        registrationRequest.valid();
        if (userRepository.isSuchUserExist(registrationRequest.getEmail(), registrationRequest.getNickname())) {
            throw new RegistrationException("Такой пользователь уже существует");
        }
        userRepository.writeUser(registrationRequest.getNickname(), registrationRequest.getEmail(), String.valueOf(registrationRequest.getPassword().hashCode()));
    }

    public void loginUser(Context ctx) throws SQLException {
        LoginRequest loginRequest = ctx.bodyAsClass(LoginRequest.class);
        loginRequest.valid();
        User user;
        if (loginRequest.getEmail() == null) {
            user = userRepository.getUserByEmailOrNickname(loginRequest, false);
        } else {
            user = userRepository.getUserByEmailOrNickname(loginRequest, true);
        }
        String password = user.getPassword();
        String email = user.getEmail();
        if (!Objects.equals(password, String.valueOf(loginRequest.getPassword().hashCode()))) {
            throw new LoginException("Неверный пароль");
        }
        ctx.result(jwtUtils.builder(email));
    }
}
