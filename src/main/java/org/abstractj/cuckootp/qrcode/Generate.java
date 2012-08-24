package org.abstractj.cuckootp.qrcode;

import org.abstractj.cuckootp.model.Account;
import org.abstractj.cuckootp.totp.KeyUri;
import org.abstractj.cuckootp.totp.TOTPUtils;

public class Generate {

    public static void main(String[] args) {

        Account account = new Account();
        account.setName("joe");
        account.setSecret(TOTPUtils.generateSecret());

        String data = KeyUri.format(account.getName(), account.getSecret());

        System.out.println(data);

        Barcode.generate(data);
    }


}
