package com.wardencloud.wardenstashedserver.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.wardencloud.wardenstashedserver.entities.User;
import com.wardencloud.wardenstashedserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.wardencloud.wardenstashedserver.helpers.ReflectionHelper;

@Service
public class UserService {
    @Autowired
    @Qualifier("UserRepositoryImpl")
    private UserRepository userRepository;

    public User findUserById(int id) {
        return userRepository.findById(id);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    public int addUser(String username, String email, String password) {
        // TODO: password should be encrypted before inserting it into DB
        User user = userRepository.addUser(username, email, password);
        if (user == null) {
            return -1;
        } else {
            return user.getId();
        }
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
}