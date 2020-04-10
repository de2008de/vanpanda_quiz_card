package com.wardencloud.wardenstashedserver.repositories;

import com.wardencloud.wardenstashedserver.entities.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
@Qualifier("UserRepositoryImpl")
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final String ADD_USER_ERROR_MESSAGE = "Something went wrong when adding new user";

    @Override
    public User findById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User findByUsername(String username) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User findByUserEmail(String email) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User addUser(String username, String email, String password, String salt) {
        try {
            int numUpdated = entityManager.createNativeQuery("INSERT INTO users (username, email, password, salt) VALUES (:username, :email, :password, :salt)", User.class)
                    .setParameter("username", username)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .setParameter("salt", salt)
                    .executeUpdate();
            if (numUpdated == 1) {
                User user = findByUserEmail(email);
                return user;
            } else if (numUpdated == 0) {
                return null;
            } else {
                throw new RuntimeException(ADD_USER_ERROR_MESSAGE);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User addCreditForUserById(int id, int credit) {
        User user = findById(id);
        entityManager.persist(user);
        user.addCredit(credit);
        entityManager.flush();
        entityManager.refresh(user);
        return user;
    }

    @Override
    public void changeUserEmail(int userId, String email) {
        User user = findById(userId);
        entityManager.persist(user);
        user.setEmail(email);
        entityManager.flush();
        entityManager.refresh(user);
    }

    @Override
    public void changeUsername(int userId, String username) {
        User user = findById(userId);
        entityManager.persist(user);
        user.setUsername(username);
        entityManager.flush();
        entityManager.refresh(user);
    }

    @Override
    public void changeUserPassword(int userId, String password) {
        User user = findById(userId);
        entityManager.persist(user);
        user.setPassword(password);
        entityManager.flush();
    }

    @Override
    public void setUserEmailVerified(int userId) {
        User user = findById(userId);
        entityManager.persist(user);
        user.setEmailVerified(true);
        entityManager.flush();
    }
}
