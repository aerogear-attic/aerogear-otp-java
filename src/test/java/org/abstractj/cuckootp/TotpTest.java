package org.abstractj.cuckootp;

import org.junit.Test;

public class TotpTest {

    @Test
    public void testGenerate() throws Exception {
        String keyString = "6165726f67656172";
        System.out.println(new Totp(new Secret(keyString), new Clock()).generate());
    }
}
