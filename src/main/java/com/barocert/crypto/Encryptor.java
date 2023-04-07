package com.barocert.crypto;

import com.barocert.BarocertException;

public interface Encryptor {
    public String enc(String plainText) throws BarocertException;
}
