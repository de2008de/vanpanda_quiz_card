package com.wardencloud.wardenstashedserver.firebase.repositories;

import com.wardencloud.wardenstashedserver.firebase.entities.FbUser;

import org.springframework.data.repository.CrudRepository;

public interface FbUserCrudRepository extends CrudRepository<FbUser, Long> {
    FbUser findByEmail(String email);
    FbUser findByUsername(String username);
}