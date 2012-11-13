package org.abstractj.cuckootp.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Base32Test {

    @Test
    public void testRandom() throws Exception {
        assertEquals(16, new Base32().random().length());
    }

    @Test
    public void testDecode() throws Exception {
        assertEquals(10, new Base32().decode(new Base32().random()).length);
    }
}
