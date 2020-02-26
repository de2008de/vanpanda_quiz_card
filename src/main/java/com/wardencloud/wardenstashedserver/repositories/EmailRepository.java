package com.wardencloud.wardenstashedserver.repositories;

import com.wardencloud.wardenstashedserver.entities.EmailVerifier;

public interface EmailRepository {
    void addEmailVerifier(int userId, String email, String token);
    EmailVerifier getEmailVerifierByUserId(int userId);
    EmailVerifier getEmailVerifierByToken(String token);
}