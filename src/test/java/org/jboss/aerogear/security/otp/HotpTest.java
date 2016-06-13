package org.jboss.aerogear.security.otp;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HotpTest {
    private Hotp hotp;
    private String sharedSecret = "B2374TNIQ3HKC446";

    @Before
    public void setUp() throws Exception {
        hotp = new Hotp(sharedSecret, 0);
    }

    @Test
    public void testUri() throws Exception {
        String name = "john";
        String url = String.format("otpauth://hotp/%s?secret=%s", name, sharedSecret);
        assertEquals(url, hotp.uri("john"));
    }

    @Test
    public void testUriEncoding() {
        Hotp hotp = new Hotp(sharedSecret);
        String url = String.format("otpauth://hotp/%s?secret=%s", "john%23doe", sharedSecret);
        assertEquals(url, hotp.uri("john#doe"));
    }

    @Test
    public void testLeadingZeros() throws Exception {
        final String expected = "002941";

        String secret = "R5MB5FAQNX5UIPWL";

        Hotp hotp = new Hotp(secret, 45187109L);
        String otp = hotp.now();
        assertEquals("Generated token must be zero padded", otp, expected);
    }

    @Test
    public void testNow() throws Exception {
        String otp = hotp.now();
        assertEquals(6, otp.length());
    }

    @Test
    public void testValidOtp() throws Exception {
        String otp = hotp.now();
        assertTrue("OTP is not valid", hotp.verify(otp));
    }
}
