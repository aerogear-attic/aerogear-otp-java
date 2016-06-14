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

import org.jboss.aerogear.security.otp.api.Base32;
import org.jboss.aerogear.security.otp.api.Digits;
import org.jboss.aerogear.security.otp.api.Hash;
import org.jboss.aerogear.security.otp.api.Hmac;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Hotp {

    private final String secret;
    private long counter;

    public Hotp(String secret) {
        this.secret = secret;
    }

    public Hotp(String secret, long counter) {
        this.secret = secret;
        this.counter = counter;
    }


    /**
     * Prover - To be used only on the client side
     * Retrieves the encoded URI to generated the QRCode required by Google Authenticator
     *
     * @param name Account name
     * @return Encoded URI
     */
    public String uri(String name) {
        try {
            return String.format("otpauth://%s/%s?secret=%s",
                    getClass().getSimpleName().toLowerCase(),
                    URLEncoder.encode(name, "UTF-8"),
                    secret);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves the current OTP
     *
     * @return OTP
     */
    public String now() {
        return leftPadding(hash(secret, getCurrentInterval()));
    }

    protected long getCurrentInterval() {
        return counter++;
    }

    protected int generate(long interval) {
        return hash(secret, interval);
    }

    private int hash(String secret, long interval) {
        byte[] hash = new byte[0];
        try {
            //Base32 encoding is just a requirement for google authenticator. We can remove it on the next releases.
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

        int binary = ((hash[offset] & 0x7f) << 24) |
                ((hash[offset + 1] & 0xff) << 16) |
                ((hash[offset + 2] & 0xff) << 8) |
                (hash[offset + 3] & 0xff);

        return binary % Digits.SIX.getValue();
    }

    private String leftPadding(int otp) {
        return String.format("%06d", otp);
    }

    public boolean verify(String otp) {
        return true;
    }
}
