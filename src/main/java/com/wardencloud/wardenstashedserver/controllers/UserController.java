package com.wardencloud.wardenstashedserver.controllers;

import com.alibaba.fastjson.JSONObject;
import com.wardencloud.wardenstashedserver.entities.EmailVerifier;
import com.wardencloud.wardenstashedserver.entities.User;
import com.wardencloud.wardenstashedserver.jwt.annotations.PassToken;
import com.wardencloud.wardenstashedserver.services.EmailService;
import com.wardencloud.wardenstashedserver.services.TokenService;
import com.wardencloud.wardenstashedserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    private final String TOKEN_KEY = "token";
    private final String ERROR_MESSAGES_KEY = "errorMessages";
    private final String SUCCESS_STATUS_KEY = "success";
    private final String USER_ID_KEY = "userId";
    private final String USER_NON_EXIST_MESSAGE = "user does not exist";

    @PostMapping(value = "/login")
    @PassToken
    public ResponseEntity<Object> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        JSONObject result = new JSONObject();
        JSONObject loginResult = userService.login(email, password);
        if (!loginResult.getBooleanValue("success")) {
            JSONObject errorMessages = loginResult.getJSONObject("errorMessages");
            result.put(SUCCESS_STATUS_KEY, false);
            result.put(ERROR_MESSAGES_KEY, errorMessages);
            return ResponseEntity.badRequest().body(result);
        } else {
            String token = loginResult.getString("token");
            result.put(TOKEN_KEY, token);
            result.put(SUCCESS_STATUS_KEY, true);
            return ResponseEntity.ok().body(result);
        }
    }

    @PostMapping(value = "/signup")
    @PassToken
    public ResponseEntity<Object> signUp(@RequestBody Map<String, String> payload) {
        JSONObject result = new JSONObject();
        String email = payload.get("email");
        String password = payload.get("password");
        JSONObject addUserResult = userService.addUser(email, password);
        if (!addUserResult.getBooleanValue("success")) {
            result.put("errorMessages", addUserResult.getJSONObject("errorMessages"));
            return ResponseEntity.badRequest().body(result);
        } else {
            int userId = addUserResult.getIntValue("userId");
            User user = userService.findUserById(userId);
            String token = tokenService.getToken(user);
            result.put(TOKEN_KEY, token);
            result.put(USER_ID_KEY, userId);
            return ResponseEntity.ok().body(result);
        }
    }

    @PutMapping(value = "/username")
    public ResponseEntity<Object> changeUserName(@RequestHeader("token") String token, @RequestBody JSONObject payload) {
        JSONObject result = new JSONObject();
        int userId = tokenService.getUserIdFromToken(token);
        if (userId == -1) {
            JSONObject errorMessages = new JSONObject();
            errorMessages.put(ERROR_MESSAGES_KEY, USER_NON_EXIST_MESSAGE);
            result.put("errorMessages", errorMessages);
            result.put("success", false);
            return ResponseEntity.badRequest().body(result);
        }
        String username = payload.getString("username");
        JSONObject userServiceResult = userService.changeUsername(userId, username);
        if (!userServiceResult.getBooleanValue("success")) {
            result.put("errorMessages", userServiceResult.getJSONObject("errorMessages"));
            result.put("success", false);
            return ResponseEntity.badRequest().body(result);
        } else {
            result.put("success", true);
            return ResponseEntity.ok().body(result);
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

    // @PostMapping(value = "/email")
    // public ResponseEntity<Object> changeUserEmail(@RequestHeader("token") String token, @RequestBody JSONObject payload) {
    //     JSONObject result = new JSONObject();
    //     int userId = tokenService.getUserIdFromToken(token);
    //     String email = payload.getString("email");
    //     JSONObject changeEmailResult = userService.changeUserEmail(userId, email);
    //     if (!changeEmailResult.getBooleanValue("success")) {
    //         JSONObject errorMessages = changeEmailResult.getJSONObject("errorMessages");
    //         result.put(ERROR_MESSAGES_KEY, errorMessages);
    //         result.put("success", false);
    //         return ResponseEntity.badRequest().body(result);
    //     }
    //     result.put("success", true);
    //     return ResponseEntity.ok().body(result);
    // }

    @PostMapping(value = "/password")
    public ResponseEntity<Object> changeUserPassword(@RequestHeader("token") String token, @RequestBody JSONObject payload) {
        JSONObject result = new JSONObject();
        int userId = tokenService.getUserIdFromToken(token);
        String currentPassword = payload.getString("currentPassword");
        String newPassword = payload.getString("newPassword");
        JSONObject changePasswordResult = userService.changeUserPassword(userId, currentPassword, newPassword);
        if (!changePasswordResult.getBooleanValue("success")) {
            JSONObject errorMessages = changePasswordResult.getJSONObject("errorMessages");
            result.put(ERROR_MESSAGES_KEY, errorMessages);
            result.put("success", false);
            return ResponseEntity.badRequest().body(result);
        }
        result.put("success", true);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/email_verification")
    @PassToken
    public ResponseEntity<Object> verifyEmail(HttpServletRequest request) {
        String token = request.getParameter("token");
        EmailVerifier emailVerifier = emailService.getEmailVerifierByToken(token);
        if (emailVerifier != null) {
            int userId = emailVerifier.getUserId();
            emailService.setUserEmailVerified(userId);
            return ResponseEntity.ok().body("Email verified");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
