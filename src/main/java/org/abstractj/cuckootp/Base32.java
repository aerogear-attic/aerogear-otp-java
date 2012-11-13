package org.abstractj.cuckootp;

import java.security.SecureRandom;
import java.util.Arrays;

public class Base32 {

    private static final int SECRET_SIZE = 10;

    private static final SecureRandom rand = new SecureRandom();

    public String random() {

        // Allocating the buffer
        byte[] buffer = new byte[SECRET_SIZE];

        // Filling the buffer with random numbers.
        rand.nextBytes(buffer);

        // Getting the key and converting it to Base32
        org.apache.commons.codec.binary.Base32 codec = new org.apache.commons.codec.binary.Base32();
        byte[] secretKey = Arrays.copyOf(buffer, SECRET_SIZE);
        byte[] encodedKey = codec.encode(secretKey);
        return new String(encodedKey);
    }

    public byte[] decode(String secret) {
        org.apache.commons.codec.binary.Base32 codec = new org.apache.commons.codec.binary.Base32();
        return codec.decode(secret);
    }
}
