package ru.otus.entities;

import ru.otus.authentication.Role;

import java.util.Set;

public class User {
    private String nickname;
    private String email;
    private String password;
    private Long id;
    private Set<Role> roles;

    public User(String nickname, String email, String password, Long id) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.id = id;
    }

    public User() {
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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


    public Long getId() {
        return id;
    }


    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }
}
