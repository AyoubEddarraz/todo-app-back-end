package com.todo.app.utils;

import com.todo.app.security.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    public String generateJwt(String subject, Map<String, Object> claims, long expirationTime) {

        String token = Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, JwtProperties.SECRET)
                .compact();

        return token;

    }

}
