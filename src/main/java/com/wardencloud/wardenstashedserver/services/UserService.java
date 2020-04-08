package com.wardencloud.wardenstashedserver.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.wardencloud.wardenstashedserver.entities.User;
import com.wardencloud.wardenstashedserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.wardencloud.wardenstashedserver.helpers.CommonHelper;
import com.wardencloud.wardenstashedserver.helpers.ReflectionHelper;
import com.wardencloud.wardenstashedserver.mongodb.entities.MongoUser;

@Service
public class UserService {
    @Autowired
    @Qualifier("UserRepositoryImpl")
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;

    private final int EMAIL_MAX_LENGTH = 100;
    private final int USERNAME_MIN_LENGTH = 4;
    private final int USERNAME_MAX_LENGTH = 16;
    private final int PASSWORD_MIN_LENGTH = 6;
    private final int PASSWORD_MAX_LENGTH = 16;

    private final String FIELD_IS_REQUIRED_ERROR_MESSAGE = "is required";
    private final String ERROR_MESSAGES_KEY = "errorMessages";
    private final String PASSWORD_INCORRECT_MESSAGE = "incorrect password";

    public User findUserById(int id) {
        return userRepository.findById(id);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    public JSONObject addUser(String email, String password) {
        String username = getRandomUsername();
        return addUser(username, email, password);
    }

    public JSONObject addUser(String username, String email, String password) {
        JSONObject result = new JSONObject();
        JSONObject errorMessages = new JSONObject();
        String sanitizedUsername = sanitizeUsername(username);
        String trimEmail = email.trim();
        JSONObject validationResult = validateSignUpInput(sanitizedUsername, trimEmail, password);
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

        String salt = encryptionService.getSalt();
        String encryptedPassword = encryptionService.encryptPassword(password, salt);
        User user = userRepository.addUser(sanitizedUsername, trimEmail, encryptedPassword, salt);
        MongoUser mongoUser = new MongoUser();
        mongoUser.setId(user.getId());
        MongoUser insertedMongoUser = mongoTemplate.insert(mongoUser);
        if (user == null || insertedMongoUser == null) {
            errorMessages.put("general", "failed to add new user");
            result.put("success", false);
            result.put("errorMessages", errorMessages);
            return result;
        } else {
            int userId = user.getId();
            String userEmail = user.getEmail();
            result.put("success", true);
            result.put("userId", userId);
            String emailVerifierToken = tokenService.getEmailVerifierToken(userId);
            emailService.addEmailVerifier(userId, userEmail, emailVerifierToken);
            emailService.sendVerificationEmail(userId);
            return result;
        }
    }

    public JSONObject login(String email, String password) {
        JSONObject result = new JSONObject();
        JSONObject errorMessages = new JSONObject();
        if (email == null || email.length() == 0) {
            errorMessages.put("email", FIELD_IS_REQUIRED_ERROR_MESSAGE);
        }
        if (password == null || password.length() == 0) {
            errorMessages.put("password", FIELD_IS_REQUIRED_ERROR_MESSAGE);
        }
        if (!errorMessages.isEmpty()) {
            result.put("success", false);
            result.put(ERROR_MESSAGES_KEY, errorMessages);
        }

        User user = findUserByEmail(email);
        if (user == null) {
            result.put("success", false);
            errorMessages.put("general", PASSWORD_INCORRECT_MESSAGE);
        } else {
            String salt = user.getSalt();
            String encryptedPassword = encryptionService.encryptPassword(password, salt);
            String userPassword = user.getPassword();
            if (!userPassword.equals(encryptedPassword)) {
                result.put("success", false);
                errorMessages.put("general", PASSWORD_INCORRECT_MESSAGE);
            }
        }

        if (!errorMessages.isEmpty()) {
            result.put("success", false);
            result.put(ERROR_MESSAGES_KEY, errorMessages);
        } else {
            String token = tokenService.getToken(user);
            result.put("token", token);
            result.put("success", true);
        }

        return result;
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
            fields = new String[] { "id", "username", "email", "credit" };
        } else {
            fields = new String[] { "id", "username" };
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

    public JSONObject changeUserEmail(int userId, String email) {
        JSONObject result = new JSONObject();
        JSONObject errorMessages = new JSONObject();
        String trimEmail = email.trim();
        User user = findUserById(userId);
        if (user == null) {
            errorMessages.put("general", PASSWORD_INCORRECT_MESSAGE);
            result.put(ERROR_MESSAGES_KEY, errorMessages);
            result.put("success", false);
            return result;
        }

        User existingUser = userRepository.findByUserEmail(trimEmail);
        if (existingUser != null) {
            errorMessages.put("email", "is already being used");
            result.put(ERROR_MESSAGES_KEY, errorMessages);
            result.put("success", false);
            return result;
        }

        if (trimEmail.length() == 0) {
            errorMessages.put("email", "is required");
        } else if (trimEmail.length() > EMAIL_MAX_LENGTH) {
            errorMessages.put("email", "is too long");
        } else if (!isEmailFormatValid(trimEmail)) {
            errorMessages.put("email", "format is incorrect");
        }

        if (!errorMessages.isEmpty()) {
            result.put(ERROR_MESSAGES_KEY, errorMessages);
            result.put("success", false);
            return result;
        }

        userRepository.changeUserEmail(userId, trimEmail);
        result.put("success", true);
        return result;
    }

    public JSONObject changeUserPassword(int userId, String currentPassword, String newPassword) {
        JSONObject result = new JSONObject();
        JSONObject errorMessages = new JSONObject();
        User user = findUserById(userId);
        if (user == null) {
            errorMessages.put("general", PASSWORD_INCORRECT_MESSAGE);
            result.put(ERROR_MESSAGES_KEY, errorMessages);
            result.put("success", false);
            return result;
        }

        String salt = user.getSalt();
        String userCurrentPassword = user.getPassword();
        String encryptedCurrentPassword = encryptionService.encryptPassword(currentPassword, salt);
        if (!encryptedCurrentPassword.equals(userCurrentPassword)) {
            errorMessages.put("currentPassword", "is incorrect");
        } else if (newPassword.length() == 0) {
            errorMessages.put("newPassword", "is required");
        } else if (newPassword.length() < PASSWORD_MIN_LENGTH || newPassword.length() > PASSWORD_MAX_LENGTH) {
            errorMessages.put("newPassword", "must between " + PASSWORD_MIN_LENGTH + " and " + PASSWORD_MAX_LENGTH);
        }

        if (!errorMessages.isEmpty()) {
            result.put(ERROR_MESSAGES_KEY, errorMessages);
            result.put("success", false);
            return result;
        }
        
        String encryptedNewPassword = encryptionService.encryptPassword(newPassword, salt);
        userRepository.changeUserPassword(userId, encryptedNewPassword);
        result.put("success", true);
        return result;
    }

    public JSONObject changeUsername(int id, String username) {
        JSONObject result = new JSONObject();
        JSONObject errorMessages = new JSONObject();
        User user = findUserById(id);
        if (user == null) {
            errorMessages.put("user", "user not found");
            result.put("errorMessages", errorMessages);
            result.put("success", false);
            return result;
        }
        String sanitizedUsername = sanitizeUsername(username);
        JSONObject usernameValidationResult = validateUsername(sanitizedUsername);
        if (!usernameValidationResult.getBooleanValue("success")) {
             CommonHelper ch = new CommonHelper();
             ch.moveJSONObjectEntries(usernameValidationResult.getJSONObject("errorMessages"), errorMessages);
             ch = null;
        }
        if (!errorMessages.isEmpty()) {
            result.put("success", false);
            result.put("errorMessages", errorMessages);
            return result;
        }
        userRepository.changeUsername(id, sanitizedUsername);
        result.put("success", true);
        return result;
    }

    private JSONObject validateSignUpInput(String username, String email, String password) {
        JSONObject result = new JSONObject();
        JSONObject errorMessages = new JSONObject();

        JSONObject usernameValidationRsult = validateUsername(username);
        if (!usernameValidationRsult.getBooleanValue("success")) {
            CommonHelper ch = new CommonHelper();
            ch.moveJSONObjectEntries(usernameValidationRsult.getJSONObject("errorMessages"), errorMessages);
            ch = null;
        }

        String trimEmail = email.trim();
        if (trimEmail.length() == 0) {
            errorMessages.put("email", "is required");
        } else if (trimEmail.length() > EMAIL_MAX_LENGTH) {
            errorMessages.put("email", "is too long");
        } else if (!isEmailFormatValid(trimEmail)) {
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

    private boolean isEmailFormatValid(String email) {
        String emailPattern = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return Pattern.matches(emailPattern, email);
    }

    private String getRandomUsername() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        StringBuilder sb = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) 
              (random.nextFloat() * (rightLimit - leftLimit + 1));
            sb.append((char) randomLimitedInt);
        }
        for (int i = 0; i < targetStringLength; i++) {
            int randomNum = random.nextInt(9);
            sb.append(randomNum);
        }
        return sb.toString();
    };

    private String sanitizeUsername(String username) {
        String sanitizedUsername = username.trim();
        return sanitizedUsername;
    }

    private JSONObject validateUsername(String username) {
        JSONObject result = new JSONObject();
        JSONObject errorMessages = new JSONObject();
        String usernamePattern = "^[a-zA-Z0-9]+$";
        boolean isUsernameMatch = Pattern.matches(usernamePattern, username);
        if (username.length() == 0) {
            errorMessages.put("username", "username is required");
        } else if (username.length() < USERNAME_MIN_LENGTH || username.length() > USERNAME_MAX_LENGTH) {
            errorMessages.put("username", "username must between " + USERNAME_MIN_LENGTH + " and " + USERNAME_MAX_LENGTH);
        } else if (!isUsernameMatch) {
            errorMessages.put("username", "username must be alphanimeric only");
        } else {
            User existingUser = findUserByUsername(username);
            if (existingUser != null) {
                errorMessages.put("username", "username is already taken");
            }
        }
        if (!errorMessages.isEmpty()) {
            result.put("success", false);
            result.put("errorMessages", errorMessages);
            return result;
        } else {
            result.put("success", true);
            return result;
        }
    }
}