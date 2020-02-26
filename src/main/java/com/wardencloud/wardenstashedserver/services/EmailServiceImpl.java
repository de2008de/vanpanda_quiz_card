package com.wardencloud.wardenstashedserver.services;

import com.wardencloud.wardenstashedserver.entities.EmailVerifier;
import com.wardencloud.wardenstashedserver.repositories.EmailRepository;
import com.wardencloud.wardenstashedserver.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    
    @Autowired
    private MailSender mailSender;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UserRepository userRepository;

    @Async("taskExecutor")
    @Override
    public void sendVerificationEmail(int userId) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            EmailVerifier emailVerifier = getEmailVerifierByUserId(userId);
            String email = emailVerifier.getEmail();
            String token = emailVerifier.getToken();
            String text = "Please click this link to verify your email: https://server.vanpanda.com/api/v1/user/email_verification?token=" + token; 
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("Email Verification");
            simpleMailMessage.setFrom("deons@qq.com");
            simpleMailMessage.setText(text);
            mailSender.send(simpleMailMessage);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void addEmailVerifier(int userId, String email, String token) {
        emailRepository.addEmailVerifier(userId, email, token);
    }

    public EmailVerifier getEmailVerifierByUserId(int userId) {
        return emailRepository.getEmailVerifierByUserId(userId);
    }

    public EmailVerifier getEmailVerifierByToken(String token) {
        EmailVerifier emailVerifier = emailRepository.getEmailVerifierByToken(token);
        return emailVerifier;
    }

    public void setUserEmailVerified(int userId) {
        userRepository.setUserEmailVerified(userId);
    }
}
