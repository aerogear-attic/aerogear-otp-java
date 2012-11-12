package org.abstractj.cuckootp;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Totp {

    private Clock clock;
    private Secret secret;


    public Totp() {
    }

    public Totp(Secret secret, Clock clock) {
        this.clock = clock;
        this.secret = secret;
    }

    public void verify() {

    }

    public int generate() {

        byte[] hash = new byte[0];
        try {
            hash = new Hmac(Hash.SHA1, secret, clock.getCurrentInterval()).digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        return binary % Digits.SIX.getValue();
    }
}
