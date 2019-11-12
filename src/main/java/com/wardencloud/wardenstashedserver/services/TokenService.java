package com.wardencloud.wardenstashedserver.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wardencloud.wardenstashedserver.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
public class TokenService {
    @Value("${token.secret}")
    private String tokenSecret; // Change this secret in application.properties when on production

    private long TOKEN_VALID_DAYS = 7;
    private Instant tokenValidInstant = ZonedDateTime.now().plusDays(TOKEN_VALID_DAYS).toInstant();
    private Date expireDate = Date.from(tokenValidInstant);

    public String getToken(User user) {
        String token = "";
        token = JWT.create()
                .withAudience(Integer.toString(user.getId()))
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(tokenSecret));
        return token;
    }

    public int getUserIdFromToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(tokenSecret)).build();
        DecodedJWT decodedJWT = null;
        try {
            // verify will test token expiration date
            // TODO: Do not throw exception when token is invalid, should return an error response
            decodedJWT = jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            return -1;
        }

        List<String> audiences = decodedJWT.getAudience();
        String userId = audiences.get(0);
        return Integer.parseInt(userId);
    }
}
