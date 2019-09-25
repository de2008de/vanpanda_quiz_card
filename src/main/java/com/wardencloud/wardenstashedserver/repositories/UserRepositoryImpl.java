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

    public User findById(int id) {
        return entityManager.find(User.class, id);
    }

    public User findByUsername(String username) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User findByUserEmail(String email) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User addUser(String username, String email, String password) {
        try {
            int numUpdated = entityManager.createNativeQuery("INSERT INTO Users (username, email, password) VALUES (:username, :email, :password)", User.class)
                    .setParameter("username", username)
                    .setParameter("email", email)
                    .setParameter("password", password)
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
            return null;
        }
    }
}
