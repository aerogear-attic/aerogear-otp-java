package org.abstractj.cuckootp.api.totp;

import org.abstractj.cuckootp.api.model.Account;
import org.abstractj.cuckootp.api.qrcode.Barcode;

import java.io.File;
import java.io.FileOutputStream;


public class Generate {

    public static final String QRCODE_FILE_NAME = "qrcode.png";

    public static void main(String[] args) throws Exception{

        Account account = new Account();
        account.setName("joe");
        account.setSecret(SecretKey.generate());

        String data = KeyUri.format(account.getName(), account.getSecret());

        System.out.println(data);

        FileOutputStream outputStream = new FileOutputStream(new File(QRCODE_FILE_NAME));

        Barcode.generate(data, outputStream);
    }


}
