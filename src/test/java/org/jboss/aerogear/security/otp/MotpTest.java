/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

public class MotpTest {

    private final static Logger LOGGER = Logger.getLogger(MotpTest.class.getName());

    @Mock
    private Clock clock;
    private Motp motp;
    private String sharedSecret = "B2374TNIQ3HKC446";
    private String pin = "1234";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(clock.getCurrentSeconds()).thenReturn(addElapsedTime(0));
        motp = new Motp(pin, sharedSecret, clock);
    }

    private long addElapsedTime(int seconds) {
        Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        LOGGER.info("Current time: " + calendar.getTime());
        calendar.add(Calendar.SECOND, seconds);
        LOGGER.info("Updated time (+" + seconds + "): " + calendar.getTime());
        long currentTimeSeconds = calendar.getTimeInMillis() / 1000;
        return currentTimeSeconds;
    }

    @Test
    public void testNow() throws Exception {
        String otp = motp.now();
        assertEquals(6, otp.length());
    }

    @Test
    public void testValidOtp() throws Exception {
        String otp = motp.now();
        assertTrue("OTP is not valid", motp.verify(otp));
    }

    @Test
    public void testOtpAfter10seconds() throws Exception {
        String otp = motp.now();
        when(clock.getCurrentSeconds()).thenReturn(addElapsedTime(10));
        assertTrue("OTP should be valid", motp.verify(otp));
    }

    @Test
    public void testOtpAfter20seconds() throws Exception {
        String otp = motp.now();
        when(clock.getCurrentSeconds()).thenReturn(addElapsedTime(20));
        assertTrue("OTP should be valid", motp.verify(otp));
    }

    @Test
    public void testOtpAfter25seconds() throws Exception {
        String otp = motp.now();
        when(clock.getCurrentSeconds()).thenReturn(addElapsedTime(25));
        assertTrue("OTP should be valid", motp.verify(otp));
    }

    @Test
    public void testOtpAfter30seconds() throws Exception {
        String otp = motp.now();
        when(clock.getCurrentSeconds()).thenReturn(addElapsedTime(30));
        assertTrue("OTP should be valid", motp.verify(otp));
    }
    
    @Test
    public void testOtpAfter40seconds() throws Exception {
        when(clock.getCurrentSeconds()).thenReturn(addElapsedTime(0) - 1);
        String otp = motp.now();
        when(clock.getCurrentSeconds()).thenReturn(addElapsedTime(40));
        assertFalse("OTP should be invalid", motp.verify(otp));
    }

    @Test
    public void testOtpAfter50seconds() throws Exception {
        when(clock.getCurrentSeconds()).thenReturn(addElapsedTime(0) - 1);
        String otp = motp.now();
        when(clock.getCurrentSeconds()).thenReturn(addElapsedTime(50));
        assertFalse("OTP should be invalid", motp.verify(otp));
    }

    @Test
    public void testOtpAfter59seconds() throws Exception {
        when(clock.getCurrentSeconds()).thenReturn(addElapsedTime(0) - 1);
        String otp = motp.now();
        when(clock.getCurrentSeconds()).thenReturn(addElapsedTime(59));
        assertFalse("OTP should be invalid", motp.verify(otp));
    }

    @Test
    public void testOtpAfter60seconds() throws Exception {
        when(clock.getCurrentSeconds()).thenReturn(addElapsedTime(0) - 1);
        String otp = motp.now();
        when(clock.getCurrentSeconds()).thenReturn(addElapsedTime(60));
        assertFalse("OTP should be invalid", motp.verify(otp));
    }

    @Test
    public void testOtpAfter61seconds() throws Exception {
        when(clock.getCurrentSeconds()).thenReturn(addElapsedTime(0) - 1);
        String otp = motp.now();
        when(clock.getCurrentSeconds()).thenReturn(addElapsedTime(61));
        assertFalse("OTP should be invalid", motp.verify(otp));
    }
}
