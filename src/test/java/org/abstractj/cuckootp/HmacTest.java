package org.abstractj.cuckootp;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class HmacTest {

    @Test
    public void testDigest() throws Exception {
        String keyString = "3132333435363738393031323334353637383930";
        byte[] key = Hex.decodeHex(keyString.toCharArray());

        System.out.println(new Totp(new Secret(keyString), new Clock()).generate());

    }
}
