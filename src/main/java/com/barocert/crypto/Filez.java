package com.barocert.crypto;

import com.barocert.BarocertException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Filez {

  public static byte[] fileToBytesFrom(String path) throws BarocertException {

    File file = new File(path);
    byte[] bytes = new byte[(int) file.length()];

    FileInputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream(file);
      fileInputStream.read(bytes);
    } catch (Exception e) {
      throw new BarocertException(-99999999, "파일 읽기에 실패하였습니다.");
    } finally {
      if (fileInputStream != null) {
        try {
          fileInputStream.close();
        } catch (IOException e) {
          throw new BarocertException(-99999999, "파일 읽기에 실패하였습니다.");
        }
      }
    }

    return bytes;
  }
}
