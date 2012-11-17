/*
 * Copyright 2012 Bruno Oliveira, and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.abstractj.cuckootp;

import org.abstractj.cuckootp.api.Base32;
import org.abstractj.cuckootp.api.Clock;
import org.abstractj.cuckootp.api.Digits;
import org.abstractj.cuckootp.api.Hash;
import org.abstractj.cuckootp.api.Hmac;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Totp {

    private String secret;
    private Base32 base32 = new Base32();
    private Clock clock = new Clock();
    private static final int DELAY_WINDOW = 1;


    public Totp(String secret) {
        this.secret = secret;
    }

    public Totp(String secret, Clock clock) {
        this.secret = secret;
        this.clock = clock;
    }

    //TODO URI.encode
    public String uri(String name) {
        return String.format("otpauth://totp/%s?secret=%s", name, secret);
    }

    public String now() {

        byte[] hash = new byte[0];
        try {
            hash = new Hmac(Hash.SHA1, base32.decode(secret), clock.getCurrentInterval()).digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return bytesToInt(hash).toString();
    }

    //TODO duplicated method, must be removed
    public int generate(String secret, long interval) {
        byte[] hash = new byte[0];
        try {
            hash = new Hmac(Hash.SHA1, base32.decode(secret), interval).digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return bytesToInt(hash);
    }

    private Integer bytesToInt(byte[] hash) {
        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        return binary % Digits.SIX.getValue();
    }

    public boolean verify(String otp) {

        long code = Long.parseLong(otp);
        long currentInterval = clock.getCurrentInterval();
        int expectedResponse = generate(secret, currentInterval);

        if (expectedResponse == code) {
            return true;
        }

        int pastResponse = generate(secret, currentInterval - DELAY_WINDOW);

        if (pastResponse == code) {
            return true;
        }

        int futureResponse = generate(secret, currentInterval + DELAY_WINDOW);

        if (futureResponse == code) {
            return true;
        }

        return false;
    }

}
