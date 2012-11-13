package org.abstractj.cuckootp;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TotpTest {

    @Test
    public void testGenerate() throws Exception {
        String keyString = "6165726f67656172";
        int otp = new Totp(new Secret(keyString), new Clock()).generate();
        System.out.println(otp);
        assertEquals(6, Integer.toString(otp).length());
    }

    @Test
    public void testOtpIsValid() throws Exception {
        String keyString = "6165726f67656172";
        Totp totp = new Totp(new Secret(keyString), new Clock());
        int otp = totp.generate();
        assertTrue("OTP is not valid", totp.verify(otp));
    }

    @Test
    public void testOtpIsValidAfter10seconds() throws Exception {
        String keyString = "6165726f67656172";
        Totp totp = new Totp(new Secret(keyString), new Clock());
        int otp = totp.generate();
        Thread.sleep(10000L);
        assertTrue("OTP is not valid", totp.verify(otp));
    }

    @Test
    public void testOtpIsValidAfter20seconds() throws Exception {
        String keyString = "6165726f67656172";
        Totp totp = new Totp(new Secret(keyString), new Clock());
        int otp = totp.generate();
        Thread.sleep(20000L);
        assertTrue("OTP is not valid", totp.verify(otp));
    }

    @Test
    public void testOtpIsValidAfter30seconds() throws Exception {
        String keyString = "6165726f67656172";
        Totp totp = new Totp(new Secret(keyString), new Clock());
        int otp = totp.generate();
        Thread.sleep(20000L);
        assertTrue("OTP is not valid", totp.verify(otp));
    }

    @Test
    public void testOtpHasExpired() throws Exception {
        String keyString = "6165726f67656172";
        Totp totp = new Totp(new Secret(keyString), new Clock());
        int otp = totp.generate();
        Thread.sleep(40000L);
        assertFalse("OTP should be invalid", totp.verify(otp));
    }
}
