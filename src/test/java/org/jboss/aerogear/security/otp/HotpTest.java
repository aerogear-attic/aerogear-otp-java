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

import org.jboss.aerogear.security.otp.api.Counter;
import org.jboss.aerogear.security.otp.api.Ticker;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HotpTest {

  private String sharedSecret = "B2374TNIQ3HKC446";

  private Hotp hotp = new Hotp(sharedSecret, new Counter());

  @Test
  public void testUri() throws Exception {
    String name = "john";
    String url = String.format("otpauth://hotp/%s?secret=%s", name, sharedSecret);
    assertEquals(url, hotp.uri("john"));
  }

  @Test
  public void testNow() throws Exception {
    //given
    hotp = new Hotp(sharedSecret, new Ticker() {
      @Override
      public long tick() {
        return 0;
      }
    });

    //when
    String otp = hotp.now();

    //then
    assertEquals(6, otp.length());
    assertEquals("920997", otp);
  }

  @Test
  public void testValidOtp() throws Exception {
    //when
    String otp = hotp.now();

    //then
    assertTrue("OTP is not valid", hotp.verify(otp));
  }

}
