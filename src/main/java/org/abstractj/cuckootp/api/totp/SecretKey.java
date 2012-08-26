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

import org.apache.commons.codec.binary.Base32;

import java.util.Arrays;
import java.util.Random;

public class SecretKey {

    private static final int SECRET_SIZE = 10;

    private static final Random rand = new Random();

    public static String generate() {

        // Allocating the buffer
        byte[] buffer = new byte[SECRET_SIZE];

        // Filling the buffer with random numbers.
        rand.nextBytes(buffer);

        // Getting the key and converting it to Base32
        Base32 codec = new Base32();
        byte[] secretKey = Arrays.copyOf(buffer, SECRET_SIZE);
        byte[] encodedKey = codec.encode(secretKey);
        return new String(encodedKey);
    }
}
