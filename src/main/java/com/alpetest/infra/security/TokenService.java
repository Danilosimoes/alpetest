package com.alpetest.infra.security;

import com.alpetest.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${forum.jwt.secret}")
    private String secret;

    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer("alpetest")
                    .withSubject(user.getCpf())
                    .withExpiresAt(this.generetionExpirationDate())
                    .sign(algorithm);
            return token;

        } catch (JWTCreationException exception){
            throw new RuntimeException("Authentication error");
        }
    }
    public String validationToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("alpetest")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTCreationException exception){

            return  null;
        }

    }
    private Instant generetionExpirationDate(){
        return LocalDateTime.now().plusHours(4).toInstant(ZoneOffset.of("-3"));
    }
}
