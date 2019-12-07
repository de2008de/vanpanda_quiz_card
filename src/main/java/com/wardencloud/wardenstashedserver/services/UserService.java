package com.wardencloud.wardenstashedserver.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.wardencloud.wardenstashedserver.entities.User;
import com.wardencloud.wardenstashedserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import com.wardencloud.wardenstashedserver.helpers.ReflectionHelper;
import com.wardencloud.wardenstashedserver.mongodb.entities.MongoUser;

@Service
public class UserService {
    @Autowired
    @Qualifier("UserRepositoryImpl")
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final int EMAIL_MAX_LENGTH = 100;
    private final int USERNAME_MIN_LENGTH = 4;
    private final int USERNAME_MAX_LENGTH = 16;
    private final int PASSWORD_MIN_LENGTH = 6;
    private final int PASSWORD_MAX_LENGTH = 16;

    public User findUserById(int id) {
        return userRepository.findById(id);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    public JSONObject addUser(String username, String email, String password) {
        // TODO: password should be encrypted before inserting it into DB
        JSONObject result = new JSONObject();
        JSONObject errorMessages = new JSONObject();
        String trimUsername = username.trim();
        String trimEmail = email.trim();
        JSONObject validationResult = validateSignUpInput(trimUsername, trimEmail, password);
        boolean isUsernameValid = (boolean) validationResult.get("success");
        if (!isUsernameValid) {
            Set<Map.Entry<String, Object>> validationErrorMessages = validationResult
                                                            .getJSONObject("errorMessages")
                                                            .entrySet();
            Iterator<Map.Entry<String, Object>> errorMessagesIterator = validationErrorMessages.iterator();
            while(errorMessagesIterator.hasNext()) {
                Map.Entry<String, Object> errorMessagesEntry = errorMessagesIterator.next();
                String errorMessageKey = errorMessagesEntry.getKey();
                Object errorMessageValue = errorMessagesEntry.getValue();
                errorMessages.put(errorMessageKey, errorMessageValue);
            }
            result.put("success", false);
            result.put("errorMessages", errorMessages);
            return result;
        }

        User existingUser = null;
        existingUser = findUserByEmail(trimEmail);
        if (existingUser != null) {
            result.put("success", false);
            errorMessages.put("email", "is already being used");
            result.put("errorMessages", errorMessages);
            return result;
        }

        existingUser = findUserByUsername(trimUsername);
        if (existingUser != null) {
            result.put("success", false);
            errorMessages.put("username", "is already being used");
            result.put("errorMessages", errorMessages);
            return result;
        }

        User user = userRepository.addUser(trimUsername, trimEmail, password);
        MongoUser mongoUser = new MongoUser();
        mongoUser.setId(user.getId());
        MongoUser insertedMongoUser = mongoTemplate.insert(mongoUser);
        if (user == null || insertedMongoUser == null) {
            errorMessages.put("general", "failed to add new user");
            result.put("success", false);
            result.put("errorMessages", errorMessages);
            return result;
        } else {
            result.put("success", true);
            result.put("userId", user.getId());
            return result;
        }
    }

    public MongoUser addMongoUser(int userId) {
        MongoUser mongoUser = new MongoUser();
        mongoUser.setId(userId);
        mongoTemplate.insert(mongoUser);
        return mongoUser;
    }

    public User addCreditForUserById(int id, int credit) {
        return userRepository.addCreditForUserById(id, credit);
    }

    public Map<String, Object> getUserProfileById(int id, Boolean isPrivate) {
        String[] fields = null;
        if (isPrivate == true) {
            fields = new String[] { "id", "username", "email", "level", "currentExp", "nextLevelExp", "credit", "verifiedIdentity" };
        } else {
            fields = new String[] { "id", "username", "description", "level", "verifiedIdentity" };
        }
        
        HashMap<String, Boolean> fieldsMap = new HashMap<>();
        for (String field : fields) {
            fieldsMap.put(field, true);
        }
        User user = this.findUserById(id);
        if (user == null) {
            return null;
        }
        Map<String, Object> userProfile = new HashMap<>();
        Class<?> userClass = user.getClass();
        Method[] methods = userClass.getMethods();
        for (Method method : methods) {
            if (ReflectionHelper.isGetter(method)) {
                String methodName = method.getName();
                String fieldName = methodName.substring(3);
                Character firstFieldNameChar = fieldName.charAt(0);
                // Convert first character to lower case
                fieldName = fieldName.replace(firstFieldNameChar.toString(), firstFieldNameChar.toString().toLowerCase());
                if (fieldsMap.get(fieldName) != null) {
                    try {
                        userProfile.put(fieldName, method.invoke(user));
                    } catch(IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    
                }
            }
        }
        return userProfile;
    }

    private JSONObject validateSignUpInput(String username, String email, String password) {
        JSONObject result = new JSONObject();
        JSONObject errorMessages = new JSONObject();

        String trimUsername = username.trim();
        String usernamePattern = "^[a-zA-Z0-9]+$";
        boolean isUsernameMatch = Pattern.matches(usernamePattern, trimUsername);
        if (trimUsername.length() == 0) {
            errorMessages.put("username", "is required");
        } else if (trimUsername.length() < USERNAME_MIN_LENGTH || trimUsername.length() > USERNAME_MAX_LENGTH) {
            errorMessages.put("username", "must between " + USERNAME_MIN_LENGTH + " and " + USERNAME_MAX_LENGTH);
        } else if (!isUsernameMatch) {
            errorMessages.put("username", "alphanimeric only");
        }
        
        String trimEmail = email.trim();
        String emailPattern = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        boolean isEmailMatch = Pattern.matches(emailPattern, trimEmail);
        if (trimEmail.length() == 0) {
            errorMessages.put("email", "is required");
        } else if (trimEmail.length() > EMAIL_MAX_LENGTH) {
            errorMessages.put("email", "is too long");
        } else if (!isEmailMatch) {
            errorMessages.put("email", "format is incorrect");
        }

        if (password.length() == 0) {
            errorMessages.put("password", "is required");
        } else if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
            errorMessages.put("password", "must between " + PASSWORD_MIN_LENGTH + " and " + PASSWORD_MAX_LENGTH);
        }

        if (!errorMessages.isEmpty()) {
            result.put("success", false);
            result.put("errorMessages", errorMessages);
        } else {
            result.put("success", true);
        }

        return result;
    }
}