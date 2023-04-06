package com.barocert.shield;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.barocert.BarocertException;

public class AES256GCM {

    public static final int GCM_TAG_LENGTH = 16;
    
    public static String enc(String plainText, String key) throws BarocertException {
        ByteBuffer byteBuffer = null;
        // 필수 값 체크.
        if (plainText == null || plainText.trim().isEmpty()) throw new BarocertException(-99999999,"There is nothing to encrypt.");
        
        try {
            // 벡터생성.
            byte[] iv = KeyGenerator.GCMKey();
        
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
    
    private static byte[] base64Decode(String input) {
        return Base64.decode(input);
    }
    private static String base64Encode(byte[] input) {
        return Base64.encode(input);
    }

}
