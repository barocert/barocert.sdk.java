package com.barocert.crypto;

import com.barocert.BarocertException;

public class EncryptorBuilder implements Encryptor{
    
    private static EncryptorBuilder _singleTone;
    private String key;
    private String mode;
    
    public static EncryptorBuilder newInstance(String key)throws BarocertException {
        if(key == null || key.trim().isEmpty() ) throw new BarocertException(-99999999,"SecretKey가 입력되지 않았습니다.");
        EncryptorBuilder _singleTone = new EncryptorBuilder();
        _singleTone.key = key;
        return _singleTone;
    }

    @Override
    public String enc(String plainText) throws BarocertException {
        String version = System.getProperty("java.version");
        if(version == null || version.trim().isEmpty() ) throw new BarocertException(-99999999,"자바 버전을 확인할수 없습니다.");
        if(version.startsWith("1.8")) {
            this.mode = "GCM";
            return AES256GCM.enc(plainText, key);
        }else {
            this.mode = "CBC";
            return AES256CBC.enc(plainText, key);
        }
    }

    public String getMode() {
        return mode;
    }
    
}
