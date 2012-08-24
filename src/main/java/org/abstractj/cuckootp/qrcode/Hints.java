package org.abstractj.cuckootp.qrcode;

import com.google.zxing.EncodeHintType;

import java.util.Hashtable;

public class Hints {

    public static final String UTF_8 = "UTF-8";

    public static Hashtable<EncodeHintType, String> create() {
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, UTF_8);
        return hints;
    }
}
