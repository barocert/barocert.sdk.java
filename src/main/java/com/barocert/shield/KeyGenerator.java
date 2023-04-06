package com.barocert.shield;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class KeyGenerator {

    private static SecureRandom random = new SecureRandom();
    
    private static final int GCM_IV_LENGTH = 12;    
    
    private static final int CBC_IV_LENGTH = 16;    
    
    private KeyGenerator() {}
    
    public static byte[] CBCKey() {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] iv = new byte[CBC_IV_LENGTH]; // CBC IV LENGTH
        // secureRandom를 취득, nextBytes를 호출해 난수 바이트를 생성.
        random.nextBytes(iv);
        return iv;
    }
    public static byte[] GCMKey() {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] iv = new byte[GCM_IV_LENGTH]; // GCM IV LENGTH
        // secureRandom를 취득, nextBytes를 호출해 난수 바이트를 생성.
        random.nextBytes(iv);
        return iv;
    }
}
