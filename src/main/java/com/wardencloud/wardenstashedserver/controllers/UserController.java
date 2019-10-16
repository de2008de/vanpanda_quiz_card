package com.wardencloud.wardenstashedserver.controllers;

import com.alibaba.fastjson.JSONObject;
import com.wardencloud.wardenstashedserver.entities.User;
import com.wardencloud.wardenstashedserver.jwt.annotations.PassToken;
import com.wardencloud.wardenstashedserver.services.TokenService;
import com.wardencloud.wardenstashedserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {
    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    private final String TOKEN_KEY = "token";
    private final String ERROR_MESSAGES_KEY = "errorMessages";
    private final String USER_ID_KEY = "userId";
    private final String USER_NON_EXIST_MESSAGE = "user does not exist";
    private final String PASSWORD_INCORRECT_MESSAGE = "incorrect password";
    private final String ALREADY_USED_ERROR_MESSAGE = "is being used already";
    private final String FIELD_IS_REQUIRED_ERROR_MESSAGE = "is required";
    private final String FAILED_ADD_USER_MESSAGE = "failed to add new user";

    @PostMapping(value = "/login")
    @PassToken
    public ResponseEntity<Object> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        JSONObject jsonObject = new JSONObject();
        Map<String, String> errorMessages = new HashMap<>();

        if (email == null || email.length() == 0) {
            errorMessages.put("email", FIELD_IS_REQUIRED_ERROR_MESSAGE);
        }
        if (password == null || password.length() == 0) {
            errorMessages.put("password", FIELD_IS_REQUIRED_ERROR_MESSAGE);
        }
        if (!errorMessages.isEmpty()) {
            jsonObject.put(ERROR_MESSAGES_KEY, errorMessages);
            return ResponseEntity.badRequest().body(jsonObject);
        }

        User userBase = userService.findUserByEmail(email);
        if (userBase == null) {
            errorMessages.put("general", USER_NON_EXIST_MESSAGE);
        }
        if (userBase != null && !userBase.getPassword().equals(password)) {
            errorMessages.put("general", PASSWORD_INCORRECT_MESSAGE);
        }
        if (!errorMessages.isEmpty()) {
            jsonObject.put(ERROR_MESSAGES_KEY, errorMessages);
            return ResponseEntity.badRequest().body(jsonObject);
        } else {
            String token = tokenService.getToken(userBase);
            jsonObject.put(TOKEN_KEY, token);
            return ResponseEntity.ok().body(jsonObject);
        }
    }

    @PostMapping(value = "/signup")
    @PassToken
    public ResponseEntity<Object> signUp(@RequestBody Map<String, String> payload) {
        Map<String, String> errorMessages = new HashMap<>();
        JSONObject jsonObject = new JSONObject();

        String[] requiredFields = new String[] {"username", "email", "password"};
        for(int i = 0; i < requiredFields.length; i++) {
            if (payload.get(requiredFields[i]) == null || payload.get(requiredFields[i]).length() == 0) {
                errorMessages.put(requiredFields[i], FIELD_IS_REQUIRED_ERROR_MESSAGE);
            }
        }
        if (!errorMessages.isEmpty()) {
            jsonObject.put(ERROR_MESSAGES_KEY, errorMessages);
            return ResponseEntity.badRequest().body(jsonObject);
        }

        String username = payload.get("username");
        String email = payload.get("email");
        String password = payload.get("password");
        if (userService.findUserByUsername(username) != null) {
            errorMessages.put("username", ALREADY_USED_ERROR_MESSAGE);
        }
        if (userService.findUserByEmail(email) != null) {
            errorMessages.put("email", ALREADY_USED_ERROR_MESSAGE);
        }
        if (!errorMessages.isEmpty()) {
            jsonObject.put(ERROR_MESSAGES_KEY, errorMessages);
            return ResponseEntity.badRequest().body(jsonObject);
        }
        int userId = userService.addUser(username, email, password);
        if (userId == -1) {
            errorMessages.put("general", FAILED_ADD_USER_MESSAGE);
            jsonObject.put(ERROR_MESSAGES_KEY, errorMessages);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
        } else {
            User user = userService.findUserById(userId);
            String token = tokenService.getToken(user);
            jsonObject.put(TOKEN_KEY, token);
            jsonObject.put(USER_ID_KEY, userId);
            return ResponseEntity.ok().body(jsonObject);
        }
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<Object> getUserProfile(@RequestHeader("token") String token) {
        Boolean isPrivateProfile = true;
        int userId = tokenService.getUserIdFromToken(token);
        Map<String, Object> userPublicProfile = userService.getUserProfileById(userId, isPrivateProfile);
        if (userPublicProfile == null) {
            JSONObject errorMessages = new JSONObject();
            errorMessages.put(ERROR_MESSAGES_KEY, USER_NON_EXIST_MESSAGE);
            return ResponseEntity.badRequest().body(errorMessages);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", userPublicProfile);
        return ResponseEntity.ok().body(jsonObject);
    }

    @GetMapping(value = "/profile/{id}")
    @PassToken
    public ResponseEntity<Object> getUserPublicProfile(@PathVariable int id) {
        Boolean isPrivateProfile = false;
        Map<String, Object> userPublicProfile = userService.getUserProfileById(id, isPrivateProfile);
        if (userPublicProfile == null) {
            JSONObject errorMessages = new JSONObject();
            errorMessages.put(ERROR_MESSAGES_KEY, USER_NON_EXIST_MESSAGE);
            return ResponseEntity.badRequest().body(errorMessages);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", userPublicProfile);
        return ResponseEntity.ok().body(jsonObject);
    }
}
