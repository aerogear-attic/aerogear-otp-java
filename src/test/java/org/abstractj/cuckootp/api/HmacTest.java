package org.abstractj.cuckootp.api;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class HmacTest {

    private Base32 base32 = new Base32();

    @Test
    public void testDigest() throws Exception {
        byte[] hash = new Hmac(Hash.SHA1, base32.decode(base32.random()), new Clock().getCurrentInterval()).digest();
        assertEquals(20, hash.length);
    }
}
