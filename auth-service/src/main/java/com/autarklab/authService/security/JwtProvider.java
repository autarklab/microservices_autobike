package com.autarklab.authService.security;

import com.autarklab.authService.entity.AuthUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    private static final String SECRET_KEY = "J0gop1";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

    //createToken
    public String create(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + 3600000);
        return JWT.create()
                .withSubject(username)
                .withIssuer("autarklab")
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .sign(ALGORITHM);
    }

    //validateToken
    public boolean isValid(String jwt) {

        try {
            JWT.require(ALGORITHM)
                    .build()
                    .verify(jwt);
            return true;

        } catch (JWTVerificationException e ) {
            return false;
        }
    }

    //getUserToken
    public String getUsername(String jwt) {

        try {
            return JWT.require(ALGORITHM)
                    .build()
                    .verify(jwt)
                    .getSubject();

        } catch (Exception e) {
            return "Bad Token !!";
        }
    }
}
