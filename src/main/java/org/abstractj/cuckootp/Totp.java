package org.abstractj.cuckootp;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Totp {

    private int interval = 30;

    private static final int[] DIGITS_POWER
            // 0 1 2 3 4 5 6 7 8
            = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};


    public Totp() {
    }

    public Totp(int interval) {
        this.interval = interval;
    }

    private long getCurrentInterval() {
        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        return currentTimeSeconds / interval;
    }

    public void verify() {

    }

    public int generate() {

        String keyString = "3132333435363738393031323334353637383930";
        byte[] msg = ByteBuffer.allocate(8).putLong(getCurrentInterval()).array();
        byte[] hash = new byte[0];
        try {
            hash = new Hmac(Hash.SHA1, new Secret(keyString), getCurrentInterval()).digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        return binary % DIGITS_POWER[6];
    }
}
