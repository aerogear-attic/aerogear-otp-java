package org.abstractj.cuckootp;

import org.apache.commons.codec.binary.Base32;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Totp {

    private Clock clock;
    private Secret secret;

    private static final int WINDOW = 30;

    public Totp() {
    }

    public Totp(Secret secret, Clock clock) {
        this.clock = clock;
        this.secret = secret;
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

    public boolean verify(long code) {

        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret.getBytes());

        // Window is used to check codes generated in the near past.
        // You can use this value to tune how far you're willing to go.
        int window = WINDOW;

        for (int i = -window; i <= window; ++i) {
            long hash = generate();

            if (hash == code) {
                return true;
            }
        }

        // The validation code is invalid.
        return false;
    }
}
