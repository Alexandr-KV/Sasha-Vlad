package ru.otus.request;

import ru.otus.utils.RequestUtils;
import ru.otus.exception.RegistrationException;

public class RegistrationRequest {

    private String nickname;
    private String email;
    private String password;

    public RegistrationRequest() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void valid() throws RegistrationException{
        if (email == null || password == null || nickname == null){
            throw new RegistrationException("Отсутствует nickname, email или password");
        }
    }
}
