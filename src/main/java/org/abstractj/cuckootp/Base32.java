package org.abstractj.cuckootp;

import java.util.Arrays;
import java.util.Random;

public class Base32 {

    private static final int SECRET_SIZE = 10;

    private static final Random rand = new Random();

    public static String random() {

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

    public static byte[] decode(String secret){
        org.apache.commons.codec.binary.Base32 codec = new org.apache.commons.codec.binary.Base32();
        return codec.decode(secret);
    }
}
