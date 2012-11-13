package org.abstractj.cuckootp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class TotpTest {

    @Mock
    private Base32 base32;

    @Mock
    private Clock clock;

    private String sharedSecret = "B2374TNIQ3HKC446";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(base32.random()).thenReturn(sharedSecret);
    }

    private long buildTime(int seconds){
        Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("Brazil/East"));
        System.out.println("Current time: " + calendar.getTime());
        calendar.add(Calendar.SECOND, seconds);
        System.out.println("Updated time: " + calendar.getTime());
        return calendar.getTimeInMillis();
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
        int otp = new Totp(base32.random(), new Clock()).generate();
        assertEquals(6, Integer.toString(otp).length());
    }

    @Test
    public void testOtpIsValid() throws Exception {
        String secret = base32.random();
        Totp totp = new Totp(secret, new Clock());
        int otp = totp.generate();
        assertTrue("OTP is not valid", totp.verify(otp));
    }

    @Test
    public void testOtpIsValidAfter10seconds() throws Exception {
        String secret = base32.random();
        Totp totp = new Totp(secret, new Clock());
        int otp = totp.generate();
        when(clock.getCurrentInterval()).thenReturn(buildTime(10));
        totp = new Totp(secret, clock);
        assertTrue("OTP is not valid", totp.verify(otp));
    }

    @Test
    public void testOtpIsValidAfter20seconds() throws Exception {
        String secret = base32.random();
        Totp totp = new Totp(secret, new Clock());
        int otp = totp.generate();
        when(clock.getCurrentInterval()).thenReturn(buildTime(20));
        totp = new Totp(secret, clock);
        assertTrue("OTP is not valid", totp.verify(otp));
    }

    @Test
    public void testOtpIsValidAfter25seconds() throws Exception {
        String secret = base32.random();
        Totp totp = new Totp(secret, new Clock());
        int otp = totp.generate();
        when(clock.getCurrentInterval()).thenReturn(buildTime(25));
        totp = new Totp(secret, clock);
        assertTrue("OTP is not valid", totp.verify(otp));
    }

    @Test
    public void testOtpIsValidAfter30seconds() throws Exception {
        String secret = base32.random();
        Totp totp = new Totp(secret, new Clock());
        int otp = totp.generate();
        when(clock.getCurrentInterval()).thenReturn(buildTime(30));
        totp = new Totp(secret, clock);
        assertTrue("OTP is not valid", totp.verify(otp));
    }

    @Test
    public void testOtpHasExpired() throws Exception {
        String secret = base32.random();
        Totp totp = new Totp(secret, new Clock());
        int otp = totp.generate();
        when(clock.getCurrentInterval()).thenReturn(buildTime(40));
        totp = new Totp(secret, clock);
        assertFalse("OTP should be invalid", totp.verify(otp));
    }
}
