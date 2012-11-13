package org.abstractj.cuckootp;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TotpTest {

    private final long fixedInstant = 1587051329670L;
    private final DateTimeZone defaultTimeZone = DateTimeZone.getDefault();

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(fixedInstant);
        DateTimeZone.setDefault(DateTimeZone.forID("Brazil/East"));
        Locale.setDefault(Locale.forLanguageTag("en_US"));
        System.out.println(DateTime.now());
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(defaultTimeZone);
    }

    private long updateTime(int seconds) {
        return new DateTime(fixedInstant).plusSeconds(seconds).getMillis();
    }

    @Test
    public void testGenerate() throws Exception {
        String keyString = "6165726f67656172";
        int otp = new Totp(new Secret(keyString), new Clock()).generate();
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
        DateTimeUtils.setCurrentMillisFixed(updateTime(10));
        assertTrue("OTP is not valid", totp.verify(otp));
    }

    @Test
    public void testOtpIsValidAfter20seconds() throws Exception {
        String keyString = "6165726f67656172";
        Totp totp = new Totp(new Secret(keyString), new Clock());
        int otp = totp.generate();
        DateTimeUtils.setCurrentMillisFixed(updateTime(20));
        assertTrue("OTP is not valid", totp.verify(otp));
    }

    @Test
    public void testOtpIsValidAfter30seconds() throws Exception {
        String keyString = "6165726f67656172";
        Totp totp = new Totp(new Secret(keyString), new Clock());
        int otp = totp.generate();
        DateTimeUtils.setCurrentMillisFixed(updateTime(30));
        assertTrue("OTP is not valid", totp.verify(otp));
    }

    @Test
    public void testOtpHasExpired() throws Exception {
        String keyString = "6165726f67656172";
        Totp totp = new Totp(new Secret(keyString), new Clock());
        int otp = totp.generate();
        DateTimeUtils.setCurrentMillisFixed(updateTime(40));
        System.out.println(DateTime.now());
        assertFalse("OTP should be invalid", totp.verify(otp));
    }
}
