package org.abstractj.cuckootp.totp;

import org.apache.commons.codec.binary.Base32;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

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