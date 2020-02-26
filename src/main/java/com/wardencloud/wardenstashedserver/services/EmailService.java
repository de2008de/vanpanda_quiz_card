package com.wardencloud.wardenstashedserver.services;

import com.wardencloud.wardenstashedserver.entities.EmailVerifier;

public interface EmailService {
    void sendVerificationEmail(int userId);
    void addEmailVerifier(int userId, String email, String token);
    EmailVerifier getEmailVerifierByUserId(int userId);
    EmailVerifier getEmailVerifierByToken(String token);
    void setUserEmailVerified(int userId);
}
