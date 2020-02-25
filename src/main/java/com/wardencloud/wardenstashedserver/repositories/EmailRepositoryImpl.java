package com.wardencloud.wardenstashedserver.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.wardencloud.wardenstashedserver.entities.EmailVerifier;

import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class EmailRepositoryImpl implements EmailRepository {
    @PersistenceContext
    private EntityManager entityManager;
    
    public void addEmailVerifier(int userId, String email, String token) {
        EmailVerifier emailVerifier = new EmailVerifier();
        entityManager.persist(emailVerifier);
        emailVerifier.setUserId(userId);
        emailVerifier.setEmail(email);
        emailVerifier.setToken(token);
        entityManager.flush();
    }

    public EmailVerifier getEmailVerifierByUserId(int userId) {
        EmailVerifier emailVerifier = entityManager.createQuery("SELECT e FROM EmailVerifier e WHERE e.userId = :userId", EmailVerifier.class)
                                        .setParameter("userId", userId)
                                        .getSingleResult();
        return emailVerifier;
    }

    public EmailVerifier getEmailVerifierByToken(String token) {
        EmailVerifier emailVerifier = entityManager.createQuery("SELECT e FROM EmailVerifier e WHERE e.token = :token", EmailVerifier.class)
                                        .setParameter("token", token)
                                        .getSingleResult();
        return emailVerifier;
    }
}
