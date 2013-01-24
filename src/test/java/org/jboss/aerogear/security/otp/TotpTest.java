/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.aerogear.security.otp;

import org.jboss.aerogear.security.otp.api.Clock;
import org.junit.Before;
import org.junit.Test;
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

    private final static Logger LOGGER = Logger.getLogger(TotpTest.class.getName());

    @Mock
    private Clock clock;
    private Totp totp;
    private String sharedSecret = "B2374TNIQ3HKC446";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(0));
        totp = new Totp(sharedSecret, clock);
    }

    private long addElapsedTime(int seconds) {
        Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        LOGGER.info("Current time: " + calendar.getTime());
        calendar.add(Calendar.SECOND, seconds);
        LOGGER.info("Updated time (+" + seconds + "): " + calendar.getTime());
        long currentTimeSeconds = calendar.getTimeInMillis() / 1000;
        return currentTimeSeconds / 30;
    }

    @Test
    public void testUri() throws Exception {
        String name = "john";
        String url = String.format("otpauth://totp/%s?secret=%s", name, sharedSecret);
        assertEquals(url, totp.uri("john"));
    }

    @Test
    public void testUriEncoding() {
        Totp totp = new Totp(sharedSecret);
        String url = String.format("otpauth://totp/%s?secret=%s", "john%23doe", sharedSecret);
        assertEquals(url, totp.uri("john#doe"));
    }

    @Test
    public void testLeadingZeros() throws Exception {
        final String expected = "002941";

        when(clock.getCurrentInterval()).thenReturn(45187109L);
        String secret = "R5MB5FAQNX5UIPWL";

        Totp totp = new Totp(secret, clock);
        String otp = totp.now();
        assertEquals("Generated token must be zero padded", otp, expected);
    }

    @Test
    public void testCustomInterval() throws Exception {
        Clock customClock = new Clock(20);
        totp = new Totp(sharedSecret, customClock);
        totp.now();
    }

    @Test
    public void testNow() throws Exception {
        String otp = totp.now();
        assertEquals(6, otp.length());
    }

    @Test
    public void testValidOtp() throws Exception {
        String otp = totp.now();
        assertTrue("OTP is not valid", totp.verify(otp));
    }

    @Test
    public void testOtpAfter10seconds() throws Exception {
        String otp = totp.now();
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(10));
        assertTrue("OTP should be valid", totp.verify(otp));
    }

    @Test
    public void testOtpAfter20seconds() throws Exception {
        String otp = totp.now();
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(20));
        assertTrue("OTP should be valid", totp.verify(otp));
    }

    @Test
    public void testOtpAfter25seconds() throws Exception {
        String otp = totp.now();
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(25));
        assertTrue("OTP should be valid", totp.verify(otp));
    }

    @Test
    public void testOtpAfter30seconds() throws Exception {
        String otp = totp.now();
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(30));
        assertTrue("OTP should be valid", totp.verify(otp));
    }

    @Test
    public void testOtpAfter31seconds() throws Exception {
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(0) - 1);
        String otp = totp.now();
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(31));
        assertFalse("OTP should be invalid", totp.verify(otp));
    }

    @Test
    public void testOtpAfter32seconds() throws Exception {
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(0) - 1);
        String otp = totp.now();
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(31));
        assertFalse("OTP should be invalid", totp.verify(otp));
    }

    @Test
    public void testOtpAfter40seconds() throws Exception {
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(0) - 1);
        String otp = totp.now();
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(40));
        assertFalse("OTP should be invalid", totp.verify(otp));
    }

    @Test
    public void testOtpAfter50seconds() throws Exception {
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(0) - 1);
        String otp = totp.now();
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(50));
        assertFalse("OTP should be invalid", totp.verify(otp));
    }

    @Test
    public void testOtpAfter59seconds() throws Exception {
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(0) - 1);
        String otp = totp.now();
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(59));
        assertFalse("OTP should be invalid", totp.verify(otp));
    }

    @Test
    public void testOtpAfter60seconds() throws Exception {
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(0) - 1);
        String otp = totp.now();
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(60));
        assertFalse("OTP should be invalid", totp.verify(otp));
    }

    @Test
    public void testOtpAfter61seconds() throws Exception {
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(0) - 1);
        String otp = totp.now();
        when(clock.getCurrentInterval()).thenReturn(addElapsedTime(61));
        assertFalse("OTP should be invalid", totp.verify(otp));
    }
}
