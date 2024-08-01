package ru.otus.utils;

import io.javalin.http.Context;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class JwtUtils {
    SecretKey key = Jwts.SIG.HS256.key().build();

    public String builder(String email) {
        return Jwts.builder().subject(email).signWith(key).compact();
    }

    public void parse(Context ctx) {
        var token = ctx.header("Authorization");
        String email = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
        RequestUtils.info("Запрос пришел от пользователя: " + email); //без конкатенации здесь не получается - выдет ошибку
    }
}
