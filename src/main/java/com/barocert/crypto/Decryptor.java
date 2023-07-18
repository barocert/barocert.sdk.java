package com.barocert.crypto;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;

import com.barocert.BarocertException;

public class Decryptor{

    private PrivateKey privateKey;
    private String mode;

    // RSA PrivateKey Set
    public static Decryptor newInstance(String key) throws BarocertException {
        if(key == null || key.trim().isEmpty() ) throw new BarocertException(-99999999,"RSA Key가 입력되지 않았습니다.");
        
        Decryptor _decryptor = new Decryptor();        
        try {
            key = key.replace("-----BEGIN RSA PRIVATE KEY-----", "").replaceAll(System.lineSeparator(), "").replaceAll("\\n", "")
                    .replace("-----END RSA PRIVATE KEY-----", "");
            byte[] bkey = Base64.decode(key);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(bkey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            
            _decryptor.privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        } catch(Exception e) {
            throw new BarocertException(-99999999,"RSA Key Instance Exception", e);
        }
        
        return _decryptor;
    }

    public String dec(String cipherText) throws BarocertException {
        if(cipherText == null || cipherText.trim().isEmpty() ) return null;

        return dec(cipherText, "AES");
    }

    public String dec(String cipherText, String algorithm) throws BarocertException {
        if(cipherText == null || cipherText.trim().isEmpty() ) return null;

        if ("AES".equals(algorithm)) {
            if("GCM".equals(mode)) 
                throw new BarocertException(-99999999,"지원하지 않는 복호화 알고리즘입니다.");
            else
                throw new BarocertException(-99999999,"지원하지 않는 복호화 알고리즘입니다.");
        }
        else if ("RSA".equals(algorithm))
            return decRSA(cipherText);
        else
            throw new BarocertException(-99999999,"지원하지 않는 복호화 알고리즘입니다.");
    }

    // RSA Decrypt
    public String decRSA(String cipherText) throws BarocertException {
        
        if(cipherText == null || cipherText.trim().isEmpty() ) throw new BarocertException(-99999999,"There is nothing to decrypt.");

        String decryptedText = "";

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] encData = Base64.decode(cipherText);
            byte[] decryptedData = cipher.doFinal(encData);
            decryptedText = new String(decryptedData, "utf-8");
        } catch(Exception e) {
            throw new BarocertException(-99999999,"RSA Decrypt Exception", e);
        }

        return decryptedText;
    }
    
}
