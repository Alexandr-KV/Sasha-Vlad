package ru.otus.request;

import ru.otus.exception.LoginException;
import ru.otus.exception.RegistrationException;

public class LoginRequest {
    private String nickname;
    private String email;
    private String password;

    public LoginRequest() {
    }

    public String getNickname() {
        return nickname;
    }


    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }

    public void valid() throws RegistrationException {
        if (email == null && nickname == null){
            throw new LoginException("Отсутствует nickname и email");
        }
        if (password == null){
            throw new LoginException("Отсутствует password");
        }
    }
}
