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

import org.jboss.aerogear.security.otp.api.Base32;
import org.jboss.aerogear.security.otp.api.Clock;
import org.jboss.aerogear.security.otp.api.Digits;
import org.jboss.aerogear.security.otp.api.Hash;
import org.jboss.aerogear.security.otp.api.Hmac;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Totp {

    private final String secret;
    private final Clock clock;
    private static final int DELAY_WINDOW = 1;

    public Totp(String secret) {
        this.secret = secret;
        clock = new Clock();
    }

    public Totp(String secret, Clock clock) {
        this.secret = secret;
        this.clock = clock;
    }

    public String uri(String name) {
        try {
            return String.format("otpauth://totp/%s?secret=%s", URLEncoder.encode(name, "UTF-8"), secret);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public String now() {
        return Integer.toString(hash(secret, clock.getCurrentInterval()));
    }
    
    public int generate(String secret, long interval) {
        return hash(secret, interval);
    }
    
    private int hash(String secret, long interval) {
        byte[] hash = new byte[0];
        try {
            hash = new Hmac(Hash.SHA1, Base32.decode(secret), interval).digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Base32.DecodingException e) {
            e.printStackTrace();
        }
        return bytesToInt(hash);
    }

    private int bytesToInt(byte[] hash) {
        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        return binary % Digits.SIX.getValue();
    }

    public boolean verify(String otp) {

        long code = Long.parseLong(otp);
        long currentInterval = clock.getCurrentInterval();

        int pastResponse = Math.max(DELAY_WINDOW, 0);
        int futureResponse = Math.max(DELAY_WINDOW, 0);

        for (int i = -pastResponse; i <= futureResponse; ++i) {
            int secret = generate(this.secret, currentInterval - i);
            if (secret == code) {
                return true;
            }
        }
        return false;
    }
}
