package com.wardencloud.wardenstashedserver.firebase.repositories;

import com.wardencloud.wardenstashedserver.firebase.entities.FbUser;

public interface FbUserRepository {
    FbUser findById(Long id);
    FbUser findByUsername(String username);
    FbUser findByUserEmail(String email);
    FbUser addUser(String username, String email, String password, String salt);
    void updateUser(FbUser user);
    void changeUserEmail(Long userId, String email);
    void changeUsername(Long userId, String username);
    void changeUserPassword(Long userId, String password);
    void setUserEmailVerified(Long userId);
}