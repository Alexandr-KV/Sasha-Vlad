package ru.otus.utils;


import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class JwtUtils {
    private final SecretKey key;

    public JwtUtils(SecretKey key) {
        this.key = key;
    }

    public String builder(String email) {
        return Jwts.builder().subject(email).signWith(key).compact();
    }

    public String parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
