package org.abstractj.cuckootp;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class Secret {

    private String key;

    public Secret(String key) {
        this.key = key;
    }

    public byte[] getBytes() {
        byte[] decodedKey = null;
        try {
            decodedKey = Hex.decodeHex(key.toCharArray());
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return decodedKey;
    }
}
