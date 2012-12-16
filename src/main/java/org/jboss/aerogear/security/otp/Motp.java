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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.jboss.aerogear.security.otp.api.Clock;

/**
 * @author Daniel Manzke
 */
public class Motp {

    private final String secret;
    private final Clock clock;
	private final String pin;
    private static final int DEFAULT_DELAY_WINDOW = 3; //latest 60 seconds -> motp.sourceforge.net tells 3 minutes past/future

    /**
     * Initialize an OTP instance with the shared secret generated on Registration process
     *
     * @param secret Shared secret
     */
    public Motp(String pin, String secret) {
        this.pin = pin;
        this.secret = secret;
        clock = new Clock();
    }

    /**
     * Initialize an OTP instance with the shared secret generated on Registration process
     *
     * @param secret Shared secret
     * @param clock  Clock responsible for retrieve the current interval
     */
    public Motp(String pin, String secret, Clock clock) {
    	this.pin = pin;
    	this.secret = secret;
        this.clock = clock;
    }

    /**
     * Retrieves the current OTP
     *
     * @return OTP
     * @throws NoSuchAlgorithmException 
     * @throws UnsupportedEncodingException 
     */
    public String now() throws NoSuchAlgorithmException, UnsupportedEncodingException {
    	return generate(this.pin, this.secret, clock.getCurrentSeconds());
    }

    /**
     * Verifier - To be used only on the server side
     * <p/>
     * Taken from Google Authenticator with small modifications from
     * {@see <a href="http://code.google.com/p/google-authenticator/source/browse/src/com/google/android/apps/authenticator/PasscodeGenerator.java?repo=android#212">PasscodeGenerator.java</a>}
     * <p/>
     * Verify a timeout code. The timeout code will be valid for a time
     * determined by the interval period and the number of adjacent intervals
     * checked.
     *
     * @param otp Timeout code
     * @return True if the timeout code is valid
     *         <p/>
     *         Author: sweis@google.com (Steve Weis)
     */
    public boolean verify(String otp) {
        return verify(otp, DEFAULT_DELAY_WINDOW);
    }
    
    public boolean verify(String otp, int delayWindow) {
        long currentSeconds = clock.getCurrentSeconds();

        int pastResponse = Math.max(delayWindow, 0) * 10;

        for (int i = pastResponse; i >= 0; i = i - 10) {
            String candidate = generate(this.pin, this.secret, currentSeconds - i);
            if (otp.equalsIgnoreCase(candidate)) {
                return true;
            }
        }
        
        return false;
    }
    
    private String generate(String pin, String secret, long epoch){
    	return hash(pin, secret, epoch).substring(0,6);
    }

    private String hash(String pin, String secret, long epoch) {
    	String hash;
    	try {
			String base = Long.toString(epoch / 10) + secret + pin;
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] bytes = digest.digest(base.getBytes("UTF-8"));
			
			return Hex.encodeHexString(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			hash = "";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			hash = "";
		}
    	return hash;
    }
}
