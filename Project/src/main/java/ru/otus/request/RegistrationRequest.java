package ru.otus.request;

import ru.otus.exception.RegistrationException;

public class RegistrationRequest {

    private String nickname;
    private String email;
    private String password;


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
        if (email == null || password == null || nickname == null) {
            throw new RegistrationException("Отсутствует nickname, email или password");
        }
        if (!email.matches("^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$")) {
            throw new RegistrationException("Неправильный email");
        }
    }
}
