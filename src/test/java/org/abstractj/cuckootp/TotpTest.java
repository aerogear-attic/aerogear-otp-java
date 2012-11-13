package org.abstractj.cuckootp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.logging.Logger;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class TotpTest {

    @Mock
    private Base32 base32;
    @Mock
    private Clock clock;
    @InjectMocks
    private Totp totp;

    private final static Logger LOGGER = Logger.getLogger(TotpTest.class.getName());

    private String sharedSecret = "B2374TNIQ3HKC446";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(base32.random()).thenReturn(sharedSecret);
        totp = new Totp(base32.random(), new Clock());
    }

    private long addInterval(int seconds) {
        Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        LOGGER.info("Current time: " + calendar.getTime());
        calendar.add(Calendar.SECOND, seconds);
        LOGGER.info("Updated time: " + calendar.getTime());
        long currentTimeSeconds = calendar.getTimeInMillis() / 1000;
        return currentTimeSeconds / 30;
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
        String secret = base32.random();
        Totp totp = new Totp(secret);
        int otp = totp.generate();
        assertEquals(6, Integer.toString(otp).length());
    }

    @Test
    public void testOtpIsValid() throws Exception {
        String secret = base32.random();
        Totp totp = new Totp(secret);
        int otp = totp.generate();
        assertTrue("OTP is not valid", totp.verify(otp));
    }

    @Test
    public void testOtpIsValidAfter10seconds() throws Exception {
        int otp = totp.generate();
        when(clock.getCurrentInterval()).thenReturn(addInterval(10));
        totp = new Totp(base32.random(), clock);
        assertTrue("OTP should be valid", totp.verify(otp));
    }

    @Test
    public void testOtpIsValidAfter20seconds() throws Exception {
        int otp = totp.generate();
        when(clock.getCurrentInterval()).thenReturn(addInterval(20));
        totp = new Totp(base32.random(), clock);
        assertTrue("OTP should be valid", totp.verify(otp));
    }

    @Test
    public void testOtpIsValidAfter25seconds() throws Exception {
        int otp = totp.generate();
        when(clock.getCurrentInterval()).thenReturn(addInterval(25));
        totp = new Totp(base32.random(), clock);
        assertTrue("OTP should be valid", totp.verify(otp));
    }

    @Test
    public void testOtpIsValidAfter30seconds() throws Exception {
        int otp = totp.generate();
        when(clock.getCurrentInterval()).thenReturn(addInterval(30));
        totp = new Totp(base32.random(), clock);
        assertTrue("OTP should be valid", totp.verify(otp));
    }

    @Test
    public void testOtpHasElapsed40seconds() throws Exception {
        totp = new Totp(base32.random(), clock);
        int otp = totp.generate();
        when(clock.getCurrentInterval()).thenReturn(addInterval(40));
        totp = new Totp(base32.random(), clock);
        assertFalse("OTP should be invalid", totp.verify(otp));
    }

    @Test
    public void testOtpHasElapsed50seconds() throws Exception {
        totp = new Totp(base32.random(), clock);
        int otp = totp.generate();
        when(clock.getCurrentInterval()).thenReturn(addInterval(50));
        totp = new Totp(base32.random(), clock);
        assertFalse("OTP should be invalid", totp.verify(otp));
    }
}
