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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public abstract class Otp {
  protected final String secret;
  protected final Ticker ticker;

  /**
   * Initialize an OTP instance with the shared secret generated on Registration process
   *
   * @param secret Shared secret
   * @param ticker Ticker responsible for retrieve the tick (either counter or time based)
   */
  public Otp(String secret, Ticker ticker) {
    this.secret = secret;
    this.ticker = ticker;
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
      return String.format("otpauth://" + getOtpType().name().toLowerCase() + "/%s?secret=%s", URLEncoder.encode(name, "UTF-8"), secret);
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  /**
   * get the type of otp hotp or topt
   *
   * @return hotp or totp
   */
  protected abstract OtpType getOtpType();

  /**
   * Retrieves the current OTP
   *
   * @return OTP
   */
  public String now() {
    return leftPadding(hash(secret, ticker.tick()));
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
   * <p/>
   * Author: sweis@google.com (Steve Weis)
   */
  public abstract boolean verify(String otp);

  protected int generate(String secret, long interval) {
    return hash(secret, interval);
  }

  protected int hash(String secret, long interval) {
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

  protected String leftPadding(int otp) {
    return String.format("%06d", otp);
  }
}
