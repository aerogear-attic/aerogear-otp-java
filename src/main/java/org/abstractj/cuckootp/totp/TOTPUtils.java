package org.abstractj.cuckootp.totp;

import org.apache.commons.codec.binary.Base32;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class TotpUtils {

	private static final int SECRET_SIZE = 10;

	private static final int PASS_CODE_LENGTH = 6;

	private static final int INTERVAL = 30;

	private static final int WINDOW = 30;

	private static final String CRYPTO = "HmacSHA1";

	private static final Random rand = new Random();

	public static String generateSecret() {

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

	public static boolean checkCode(String secret, long code) throws NoSuchAlgorithmException, InvalidKeyException {
		Base32 codec = new Base32();
		byte[] decodedKey = codec.decode(secret);

		// Window is used to check codes generated in the near past.
		// You can use this value to tune how far you're willing to go.
		int window = WINDOW;
		long currentInterval = getCurrentInterval();

		for (int i = -window; i <= window; ++i) {
			long hash = Totp.generateTOTP(decodedKey, currentInterval + i, PASS_CODE_LENGTH, CRYPTO);

			if (hash == code) {
				return true;
			}
		}

		// The validation code is invalid.
		return false;
	}

	private static long getCurrentInterval() {
		long currentTimeSeconds = System.currentTimeMillis() / 1000;
		return currentTimeSeconds / INTERVAL;
	}

}