package com.wardencloud.wardenstashedserver.repositories;

import com.wardencloud.wardenstashedserver.entities.User;

public interface UserRepository {
    User findById(int id);
    User findByUsername(String username);
    User findByUserEmail(String email);
    User addUser(String username, String email, String password);
    User addCreditForUserById(int id, int credit);
}
