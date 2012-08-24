package org.abstractj.cuckootp.totp;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

import static org.junit.Assert.assertEquals;

public class TOTPTest {
    private static final String CRYPTO = "HmacSHA1";

    @Test
    public void generateTOTPTest() throws DecoderException {

        String keyString = "3132333435363738393031323334353637383930";
        byte[] key = Hex.decodeHex(keyString.toCharArray());

        assertEquals(generateGoogleOTP(key), generateRfcOTP(key));

    }

    private int generateRfcOTP(byte[] key) {
        return TOTP.generateTOTP(key, getCurrentInterval(), 6, CRYPTO);
    }

    private int generateGoogleOTP(byte[] key) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(CRYPTO);
            SecretKeySpec macKey = new SecretKeySpec(key, "RAW");
            hmac.init(macKey);

            PasscodeGenerator passcodeGenerator = new PasscodeGenerator(hmac);

            return Integer.parseInt(passcodeGenerator.generateTimeoutCode());

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private long getCurrentInterval() {
        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        return currentTimeSeconds / 30;
    }
}
