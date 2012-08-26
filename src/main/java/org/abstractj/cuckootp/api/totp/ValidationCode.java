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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ValidationCode {

	private static final int PASS_CODE_LENGTH = 6;

	private static final int INTERVAL = 30;

	private static final int WINDOW = 30;

	private static final String CRYPTO = "HmacSHA1";

	public static boolean check(String secret, long code) throws NoSuchAlgorithmException, InvalidKeyException {
		Base32 codec = new Base32();
		byte[] decodedKey = codec.decode(secret);

		int window = WINDOW;
		long currentInterval = getCurrentInterval();

		for (int i = -window; i <= window; ++i) {
			long hash = Totp.generateTOTP(decodedKey, currentInterval + i, PASS_CODE_LENGTH, CRYPTO);

			if (hash == code) {
				return true;
			}
		}

		return false;
	}

	private static long getCurrentInterval() {
		long currentTimeSeconds = System.currentTimeMillis() / 1000;
		return currentTimeSeconds / INTERVAL;
	}

}