package com.wardencloud.wardenstashedserver.firebase.repositories;

import com.google.cloud.datastore.Query;
import com.wardencloud.wardenstashedserver.firebase.entities.FbUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.data.datastore.core.DatastoreTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FbUserRepositoryImpl implements FbUserRepository {

    @Autowired
    private DatastoreTemplate datastoreTemplate;

    @Autowired
    private FbUserCrudRepository fbUserCrudRepository;

    @Override
    public FbUser addUser(String username, String email, String password, String salt) {
        FbUser user = new FbUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setSalt(salt);
        datastoreTemplate.save(user);
        return user;
    }

    @Override
    public void changeUserEmail(Long userId, String email) {
        FbUser user = findById(userId);
        if (user == null) {
            return;
        }
        user.setEmail(email);
        updateUser(user);
    }

    @Override
    public void changeUserPassword(Long userId, String password) {
        FbUser user = findById(userId);
        if (user == null) {
            return;
        }
        user.setPassword(password);
        updateUser(user);
    }

    @Override
    public void changeUsername(Long userId, String username) {
        FbUser user = findById(userId);
        if (user == null) {
            return;
        }
        user.setUsername(username);
        updateUser(user);
    }

    @Override
    public FbUser findById(Long id) {
        return datastoreTemplate.findById(id, FbUser.class);
    }

    @Override
    public FbUser findByUserEmail(String email) {
        return fbUserCrudRepository.findByEmail(email);
    }

    @Override
    public FbUser findByUsername(String username) {
        return fbUserCrudRepository.findByUsername(username);
    }

    @Override
    public void setUserEmailVerified(Long userId) {
        FbUser user = findById(userId);
        if (user == null) {
            return;
        }
        user.setEmailVerified(true);
        updateUser(user);
    }

    @Override
    public void updateUser(FbUser user) {
        datastoreTemplate.save(user);
    }
}
