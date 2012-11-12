package org.abstractj.cuckootp;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Hmac {

    public static final String ALGORITHM = "RAW";
    private Hash hash;
    private Secret secret;
    private long currentInterval;

    public Hmac(Hash hash, Secret secret, long currentInterval) {
        this.hash = hash;
        this.secret = secret;
        this.currentInterval = currentInterval;
    }

    public byte[] digest() throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] challenge = ByteBuffer.allocate(8).putLong(currentInterval).array();
        Mac mac = Mac.getInstance(hash.toString());
        SecretKeySpec macKey = new SecretKeySpec(secret.getBytes(), ALGORITHM);
        mac.init(macKey);
        return mac.doFinal(challenge);
    }
}
