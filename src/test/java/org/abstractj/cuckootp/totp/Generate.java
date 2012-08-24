package org.abstractj.cuckootp.totp;

import org.abstractj.cuckootp.model.Account;
import org.abstractj.cuckootp.qrcode.Barcode;


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
