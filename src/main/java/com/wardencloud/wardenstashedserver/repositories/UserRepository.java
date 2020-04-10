package com.wardencloud.wardenstashedserver.repositories;

import com.wardencloud.wardenstashedserver.entities.User;

public interface UserRepository {
    User findById(int id);
    User findByUsername(String username);
    User findByUserEmail(String email);
    User addUser(String username, String email, String password, String salt);
    User addCreditForUserById(int id, int credit);
    void changeUserEmail(int userId, String email);
    void changeUsername(int userId, String username);
    void changeUserPassword(int userId, String password);
    void setUserEmailVerified(int userId);
}
