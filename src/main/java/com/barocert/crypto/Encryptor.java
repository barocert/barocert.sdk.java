package com.barocert.crypto;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.barocert.BarocertException;

public class Encryptor{
    
    private static final int GCM_TAG_LENGTH = 16;
    private static final int GCM_IV_LENGTH = 12;    
    private static final int CBC_IV_LENGTH = 16;    

    private static SecureRandom random = new SecureRandom();
    private String key;
    private String mode;
    
    public static Encryptor newInstance(String key)throws BarocertException {
        if(key == null || key.trim().isEmpty() ) throw new BarocertException(-99999999,"SecretKey가 입력되지 않았습니다.");
        Encryptor _singleTone = new Encryptor();
        _singleTone.key = key;
        _singleTone.mode = getAESMode();
        return _singleTone;
    }

    // java ver 1.8 미만 운영모드 CBC, java ver 1.8 이상 운영모드 GCM
    private static String getAESMode() throws BarocertException {
        String version = System.getProperty("java.version");
        if(version == null || version.trim().isEmpty() ) throw new BarocertException(-99999999,"자바 버전을 확인할수 없습니다.");
        if(version.startsWith("1.6") || version.startsWith("1.7") ) {
            return "CBC";
        }else {
            return "GCM";
        }
    }
    
    public String enc(String plainText) throws BarocertException {
        return enc(plainText, "AES");
    }
    
    public String enc(String plainText, String algorithm) throws BarocertException {
        if("AES".equals(algorithm)) {
            if("GCM".equals(mode)) {
                return encGCM(plainText, key);
            }else {
                return encCBC(plainText, key);
            }
        } else if("RSA".equals(algorithm)) {
            throw new BarocertException(-99999999,"서비스 준비중인 암호화 알고리즘 입니다.");
        } else {
            throw new BarocertException(-99999999,"지원하지 않는 암호화 알고리즘입니다.");
        }
    }

    public static String encCBC(String plainText, String key) throws BarocertException {
        ByteBuffer byteBuffer = null;
        
        // 필수 값 체크.
        if (plainText == null || plainText.trim().isEmpty()) throw new BarocertException(-99999999,"There is nothing to encrypt.");
        
        try {
            // 벡터생성.
            byte[] iv = newCBCbyte();
        
            // SecretKeySpec 객체를 이욜해서 KEY 생성.
            SecretKeySpec keySpec = new SecretKeySpec(base64Decode(key), "AES");
            
            // Cipher 초기화.
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec,new IvParameterSpec(iv));
            
            // Byte배열평문을 암호화, 복호화 된 Byte 배열로 반환.
            byte[] encryptedData = cipher.doFinal(plainText.getBytes(Charset.forName("UTF-8")));
        
            // 벡터길이와 암호문 길이만큼 할당.
            byteBuffer = ByteBuffer.allocate(iv.length + encryptedData.length);
            byteBuffer.put(iv); // 벡터 추가.
            byteBuffer.put(encryptedData); // 암호문 추가.
        } catch (Exception e) {
            throw new BarocertException(-99999999,"AES256 Encrypt Exception", e);
        }
        
        // 버퍼에 저장된 데이터를 배열로 받고, Base64 Encode.
        return base64Encode(byteBuffer.array());
    }
    
    public static byte[] newCBCbyte() {
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
    
    public static String encGCM(String plainText, String key) throws BarocertException {
        ByteBuffer byteBuffer = null;
        // 필수 값 체크.
        if (plainText == null || plainText.trim().isEmpty()) throw new BarocertException(-99999999,"There is nothing to encrypt.");
        
        try {
            // 벡터생성.
            byte[] iv = newGCMbyte();
        
            // SecretKeySpec 객체를 이욜해서 KEY 생성.
            SecretKeySpec keySpec = new SecretKeySpec(base64Decode(key), "AES");
            
            // Cipher 초기화.
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec,new GCMParameterSpec(GCM_TAG_LENGTH * 8,iv));
            
            // Byte배열평문을 암호화, 복호화 된 Byte 배열로 반환.
            byte[] encryptedData = cipher.doFinal(plainText.getBytes(Charset.forName("UTF-8")));
        
            // 벡터길이와 암호문 길이만큼 할당.
            byteBuffer = ByteBuffer.allocate(iv.length + encryptedData.length);
            byteBuffer.put(iv); // 벡터 추가.
            byteBuffer.put(encryptedData); // 암호문 추가.
        } catch (Exception e) {
            throw new BarocertException(-99999999, "AES256 Encrypt Exception", e);
        }

        // 버퍼에 저장된 데이터를 배열로 받고, Base64 Encode.
        return base64Encode(byteBuffer.array());
    }
    
    public static byte[] newGCMbyte() {
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
    
    private static byte[] base64Decode(String input) {
        return Base64.decode(input);
    }
    private static String base64Encode(byte[] input) {
        return Base64.encode(input);
    }
    
    public String getMode() {
        return mode;
    }
    
}
