package org.abstractj.cuckootp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class TotpTest {

    @Mock
    private Base32 base32;

    private String sharedSecret = "B2374TNIQ3HKC446";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(base32.random()).thenReturn(sharedSecret);
    }

    @Test
    public void testUri() throws Exception {
        Totp totp = new Totp(base32.random());
        String name = "john";
        String url = String.format("otpauth://totp/%s?secret=%s", name, sharedSecret);
        assertEquals(url, totp.uri("john"));
    }
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
