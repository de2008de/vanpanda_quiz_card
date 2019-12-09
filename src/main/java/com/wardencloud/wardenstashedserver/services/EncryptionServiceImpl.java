package com.wardencloud.wardenstashedserver.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.stereotype.Service;

@Service
public class EncryptionServiceImpl implements EncryptionService {
    private final String SHA_ALGORITHM = "SHA-256";
    private final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
    private final int SHA_LENGTH = 32;
    private final int SALT_LENGTH = 6;

    public String encryptPassword(String password, String salt) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(SHA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String saltedPassword = password + salt;
        byte[] saltedBytesPassword = saltedPassword.getBytes();
        BigInteger sha = new BigInteger(messageDigest.digest(saltedBytesPassword));
        String encryptedPassword = sha.toString(SHA_LENGTH);
        return encryptedPassword;
    }

    public String getSalt() {
        byte[] randomBytes = new byte[SALT_LENGTH];
        try {
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
            secureRandom.nextBytes(randomBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String salt = randomBytes.toString();
        return salt;
    }
}
