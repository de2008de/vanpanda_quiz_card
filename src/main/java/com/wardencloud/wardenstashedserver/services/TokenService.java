package com.wardencloud.wardenstashedserver.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wardencloud.wardenstashedserver.firebase.entities.FbUser;

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

    public String getToken(FbUser user) {
        String token = "";
        Date expiredDate = getTokenExpiredDate();
        token = JWT.create()
                .withAudience(Long.toString(user.getId()))
                .withExpiresAt(expiredDate)
                .sign(Algorithm.HMAC256(tokenSecret));
        return token;
    }

    public Long getUserIdFromToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(tokenSecret)).build();
        DecodedJWT decodedJWT = null;
        try {
            // verify will test token expiration date
            // TODO: Do not throw exception when token is invalid, should return an error response
            decodedJWT = jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            return -1l;
        }

        List<String> audiences = decodedJWT.getAudience();
        String userId = audiences.get(0);
        return Long.parseLong(userId);
    }


    public String getEmailVerifierToken(Long userId) {
        String token = JWT.create()
                        .withAudience(Long.toString(userId))
                        .sign(Algorithm.HMAC256(tokenSecret));
        return token;
    }
  
    private Date getTokenExpiredDate() {
        Instant tokenValidInstant = ZonedDateTime.now().plusDays(TOKEN_VALID_DAYS).toInstant();
        Date expiredDate = Date.from(tokenValidInstant);
        return expiredDate;
    }
}
