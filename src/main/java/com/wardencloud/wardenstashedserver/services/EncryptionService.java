package com.wardencloud.wardenstashedserver.services;

public interface EncryptionService {
    String encryptPassword(String password, String salt);
    String getSalt();
}
