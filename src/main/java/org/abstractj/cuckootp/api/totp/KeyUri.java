package org.abstractj.cuckootp.api.totp;

public class KeyUri {

    private static String domain = "abstractj.org";

    public static String format(String name, String secret) {
        String format = "otpauth://totp/%s@%s?secret=%s";

        return String.format(format, name, domain, secret);
    }
}
