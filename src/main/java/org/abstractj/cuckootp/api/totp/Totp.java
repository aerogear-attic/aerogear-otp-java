package org.abstractj.cuckootp.api.totp;

import java.nio.ByteBuffer;

public class Totp {

    private static final int[] DIGITS_POWER
            // 0 1 2 3 4 5 6 7 8
            = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    /**
     * This method generates a Totp value for the given set of parameters.
     *
     * @param key    : the shared secret
     * @param time   : a value that reflects a time
     * @param digits : number of digits to return
     * @param crypto : the crypto function to use
     * @return: digits
     */

    public static int generateTOTP(byte[] key, long time, int digits, String crypto) {

        byte[] msg = ByteBuffer.allocate(8).putLong(time).array();
        byte[] hash = KeyAuthenticatorCode.create(crypto, key, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        return binary % DIGITS_POWER[digits];
    }
}
