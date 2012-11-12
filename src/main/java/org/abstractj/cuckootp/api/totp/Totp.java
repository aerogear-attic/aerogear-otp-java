/**
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

package org.abstractj.cuckootp.api.totp;

import java.nio.ByteBuffer;

public class Totp {

    private static final int[] DIGITS_POWER
            // 0 1 2 3 4 5 6 7 8
            = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    /**
     * This method generates a Totp value for the given set of parameters.
     *
     * @param key    : the shared secret
     * @param time   : a value that reflects a time
     * @param digits : number of digits to return
     * @param crypto : the crypto function to use
     * @return: digits
     */

    public static int generateTOTP(byte[] key, long time, int digits, String crypto) {

        byte[] msg = ByteBuffer.allocate(8).putLong(time).array();
        byte[] hash = KeyAuthenticatorCode.create(crypto, key, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        return binary % DIGITS_POWER[6];
    }
}
