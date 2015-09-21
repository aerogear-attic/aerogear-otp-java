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
    private GregorianCalendar currentTime = new GregorianCalendar(2015, 5, 1, 10, 0, 0);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        long interval = getInterval(currentTime);
        LOGGER.info("Initial interval: " + interval);
        when(clock.getCurrentInterval()).thenReturn(interval);
        totp = new Totp(sharedSecret, clock);
    }

    private long getInterval(GregorianCalendar gregorianCalendar) {
        long currentTimeSeconds = gregorianCalendar.getTimeInMillis() / 1000;
        return currentTimeSeconds / 30;
    }

    private void addElapsedTime(int seconds) {
        LOGGER.info("Current time: " + currentTime.getTime());
        currentTime.add(Calendar.SECOND, seconds);
        LOGGER.info("Updated time (+" + seconds + "): " + currentTime.getTime());

        long updatedInterval = getInterval(currentTime);
        LOGGER.info("Updated interval: " + updatedInterval);
        when(clock.getCurrentInterval()).thenReturn(updatedInterval);
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
        addElapsedTime(10);
        assertTrue("OTP should be valid", totp.verify(otp));
    }

    @Test
    public void testOtpAfter30seconds() throws Exception {
        String otp = totp.now();
        addElapsedTime(30);
        assertTrue("OTP should be valid", totp.verify(otp));
    }

    @Test
    public void testOtpAfter59seconds() throws Exception {
        String otp = totp.now();
        addElapsedTime(59);
        assertTrue("OTP should be valid", totp.verify(otp));
    }

    @Test
    public void testOtpAfter60seconds() throws Exception {
        String otp = totp.now();
        addElapsedTime(60);
        assertFalse("OTP should be invalid", totp.verify(otp));
    }

    @Test
    public void testOtpAfter61seconds() throws Exception {
        String otp = totp.now();
        addElapsedTime(61);
        assertFalse("OTP should be invalid", totp.verify(otp));
    }

    @Test
    public void testOtpAfterNegative10seconds() throws Exception {
        String otp = totp.now();
        addElapsedTime(-10);
        assertTrue("OTP should be valid", totp.verify(otp));
    }

    @Test
    public void testOtpAfterNegative35seconds() throws Exception {
        String otp = totp.now();
        addElapsedTime(-35);
        assertFalse("OTP should be invalid", totp.verify(otp));
    }
}
