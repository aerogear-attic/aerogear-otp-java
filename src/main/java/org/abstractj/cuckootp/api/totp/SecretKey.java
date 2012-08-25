package org.abstractj.cuckootp.api.totp;

import org.apache.commons.codec.binary.Base32;

import java.util.Arrays;
import java.util.Random;

public class SecretKey {

    private static final int SECRET_SIZE = 10;

    private static final Random rand = new Random();

    public static String generate() {

        // Allocating the buffer
        byte[] buffer = new byte[SECRET_SIZE];

        // Filling the buffer with random numbers.
        rand.nextBytes(buffer);

        // Getting the key and converting it to Base32
        Base32 codec = new Base32();
        byte[] secretKey = Arrays.copyOf(buffer, SECRET_SIZE);
        byte[] encodedKey = codec.encode(secretKey);
        return new String(encodedKey);
    }
}
