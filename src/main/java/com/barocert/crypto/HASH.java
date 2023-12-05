package com.barocert.crypto;

import java.security.MessageDigest;
import java.util.Base64;

import com.barocert.BarocertException;

public class HASH {

    public String sha256(String target) throws BarocertException {
	
        byte[] sha256EncodedData; 
		try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(target.getBytes());
            
			sha256EncodedData = Base64.getUrlEncoder().encode(md.digest());
		} catch (Exception e) {
			throw new BarocertException(-99999999, "해시 생성에 실패하였습니다.");
		}
		
        return new String(sha256EncodedData);
	}

}
