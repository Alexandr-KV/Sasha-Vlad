package ru.otus.controller;

import io.javalin.http.Context;
import ru.otus.ProjectRepository;
import ru.otus.entities.User;
import ru.otus.exception.LoginException;
import ru.otus.exception.RegistrationException;
import ru.otus.request.LoginRequest;
import ru.otus.request.RegistrationRequest;
import ru.otus.utils.JwtUtils;

import java.sql.SQLException;
import java.util.Objects;

public class UserController {
    private final ProjectRepository projectRepository;
    private final JwtUtils jwtUtils;
    public UserController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
        this.jwtUtils = new JwtUtils();
    }

    public void registrationNewUser(Context ctx) throws SQLException,RegistrationException {
        RegistrationRequest registrationRequest = ctx.bodyAsClass(RegistrationRequest.class);
        registrationRequest.valid();
        if (projectRepository.isSuchUserExist(registrationRequest.getEmail(),registrationRequest.getNickname())){
            throw new RegistrationException("Такой пользователь уже существует");
        }
        projectRepository.writeNewUser(registrationRequest.getNickname(),registrationRequest.getEmail(),String.valueOf(registrationRequest.getPassword().hashCode()));
    }

    public void loginUser(Context ctx) throws SQLException{
        LoginRequest loginRequest = ctx.bodyAsClass(LoginRequest.class);
        loginRequest.valid();
        User user;
        if (loginRequest.getEmail() == null){
            user = projectRepository.getUserByEmailOrNickname(loginRequest,false);
        }else {
            user = projectRepository.getUserByEmailOrNickname(loginRequest,true);
        }
        String password = user.getPassword();
        String email = user.getEmail();
        if (!Objects.equals(password,String.valueOf(loginRequest.getPassword().hashCode()))){
            throw new LoginException("Неверный пароль");
        }
        ctx.result(jwtUtils.builder(email));
    }
}
