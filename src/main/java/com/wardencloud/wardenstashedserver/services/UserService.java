package com.wardencloud.wardenstashedserver.services;

import com.wardencloud.wardenstashedserver.entities.User;
import com.wardencloud.wardenstashedserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
}