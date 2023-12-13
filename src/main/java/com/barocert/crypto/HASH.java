package com.barocert.crypto;

import java.security.MessageDigest;
import com.barocert.BarocertException;

public class HASH {

	public String sha256ToBase64url(String target) throws BarocertException {

		String result;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(target.getBytes());

			result = Base64.urlEncode(md.digest());
		} catch (Exception e) {
			throw new BarocertException(-99999999, "해시 생성에 실패하였습니다.");
		}

		return result;
	}

}
