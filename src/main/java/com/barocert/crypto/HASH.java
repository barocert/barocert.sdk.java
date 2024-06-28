package com.barocert.crypto;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
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

  public String sha256ToBase64urlFileFrom(byte[] bytes) throws BarocertException {

    String result;
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(bytes);

      result = Base64.urlEncode(md.digest());
    } catch (Exception e) {
      throw new BarocertException(-99999999, "해시 생성에 실패하였습니다.");
    }

    return result;
  }

  public String sha256ToBase64urlFileFrom(FileInputStream fileInputStream) throws BarocertException {

    byte[] buffer = new byte[8192];
    int count;
    String result;
    try {

      MessageDigest md = MessageDigest.getInstance("SHA-256");
      BufferedInputStream bis = new BufferedInputStream(fileInputStream);
      while ((count = bis.read(buffer)) > 0) {
        md.update(buffer, 0, count);
      }
      bis.close();
      result = Base64.urlEncode(md.digest());
    } catch (Exception e) {
      throw new BarocertException(-99999999, "해시 생성에 실패하였습니다.");
    }

    return result;
  }

}
