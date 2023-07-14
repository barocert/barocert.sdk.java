package com.barocert.crypto;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;
import java.util.Base64;
import com.barocert.BarocertException;

public class Decryptor{

    private PrivateKey privateKey;
    
    // RSA PrivateKey Set
    public static Decryptor newInstance(String key) throws BarocertException {
        if(key == null || key.trim().isEmpty() ) throw new BarocertException(-99999999,"SecretKey가 입력되지 않았습니다.");
        
        Decryptor _decryptor = new Decryptor();        
        try {
            key = key.replace("-----BEGIN RSA PRIVATE KEY-----", "").replaceAll(System.lineSeparator(), "").replaceAll("\\n", "")
                    .replace("-----END RSA PRIVATE KEY-----", "");
            byte[] bkey = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(bkey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            
            _decryptor.privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        } catch(Exception e) {
            throw new BarocertException(-99999999,"RSA Key Instance Exception", e);
        }
        
        return _decryptor;
    }

    // RSA Decrypt
    public String Decrypt(String cipherText) throws BarocertException {
        
        if(cipherText == null || cipherText.trim().isEmpty() ) return null;

        String decryptedText;

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] encData = Base64.getDecoder().decode(cipherText.getBytes());
            byte[] decryptedData = cipher.doFinal(encData);
            decryptedText = new String(decryptedData, "utf-8");
        } catch(Exception e) {
            throw new BarocertException(-99999999,"RSA Decrypt Exception", e);
        }

        return decryptedText;
    }

}
