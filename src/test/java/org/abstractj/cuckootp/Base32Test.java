package org.abstractj.cuckootp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Base32Test {

    @Test
    public void testRandom() throws Exception {
        assertEquals(16, Base32.random().length());
    }

    @Test
    public void testDecode() throws Exception {
        assertEquals(10, Base32.decode(Base32.random()).length);
    }
}
