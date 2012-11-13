package org.abstractj.cuckootp;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Totp {

    private String secret;
    private Base32 base32 = new Base32();
    private Clock clock = new Clock();
    private static final int DELAY_WINDOW = 1;

    public Totp(String secret) {
        this.secret = secret;
    }

    public Totp(String secret, Clock clock) {
        this.secret = secret;
        this.clock = clock;
    }

    //TODO URI.encode
    public String uri(String name) {
        return String.format("otpauth://totp/%s?secret=%s", name, secret);
    }

    public int generate() {

        byte[] hash = new byte[0];
        try {
            System.out.println("Generate: " + clock.getCurrentInterval());
            hash = new Hmac(Hash.SHA1, base32.decode(secret), clock.getCurrentInterval()).digest();
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

    public int generate(String secret, long interval) {
        byte[] hash = new byte[0];
        try {
            hash = new Hmac(Hash.SHA1, base32.decode(secret), interval).digest();
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

    public boolean verify(long code) {

        long currentInterval = clock.getCurrentInterval();
        int expectedResponse = generate(secret, currentInterval);
        if (expectedResponse == code) {
            return true;
        }
        for (int i = 1; i <= DELAY_WINDOW; i++) {
            int pastResponse = generate(secret, currentInterval - i);
            if (pastResponse == code) {
                return true;
            }
        }
        for (int i = 1; i <= DELAY_WINDOW; i++) {
            int futureResponse = generate(secret, currentInterval + i);
            if (futureResponse == code) {
                return true;
            }
        }
        return false;
    }

}
