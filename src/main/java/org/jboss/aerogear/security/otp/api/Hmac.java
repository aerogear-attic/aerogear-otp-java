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

package org.jboss.aerogear.security.otp.api;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Hmac {

    public static final String ALGORITHM = "RAW";
    private final Hash hash;
    private final byte[] secret;
    private final long currentInterval;

    public Hmac(Hash hash, byte[] secret, long currentInterval) {
        this.hash = hash;
        this.secret = secret;
        this.currentInterval = currentInterval;
    }

    public byte[] digest() throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] challenge = ByteBuffer.allocate(8).putLong(currentInterval).array();
        Mac mac = Mac.getInstance(hash.toString());
        SecretKeySpec macKey = new SecretKeySpec(secret, ALGORITHM);
        mac.init(macKey);
        return mac.doFinal(challenge);
    }
}
