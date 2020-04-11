package com.wardencloud.wardenstashedserver.services;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.wardencloud.wardenstashedserver.firebase.entities.FbUser;

public interface UserService {
    FbUser findUserById(Long id);
    FbUser findUserByUsername(String username);
    FbUser findUserByEmail(String email);
    JSONObject addUser(String email, String password);
    JSONObject addUser(String username, String email, String password);
    void updateUser(FbUser user);
    JSONObject login(String email, String password);
    Map<String, Object> getUserProfileById(Long id, Boolean isPrivate);
    JSONObject changeUserEmail(Long userId, String email);
    JSONObject changeUserPassword(Long userId, String currentPassword, String newPassword);
    JSONObject changeUsername(Long id, String username);
}