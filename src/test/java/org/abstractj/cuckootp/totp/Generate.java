package org.abstractj.cuckootp.totp;

import org.abstractj.cuckootp.model.Account;
import org.abstractj.cuckootp.qrcode.Barcode;

import java.io.File;
import java.io.FileOutputStream;


public class Generate {

    public static final String QRCODE_FILE_NAME = "qrcode.png";

    public static void main(String[] args) throws Exception{

        Account account = new Account();
        account.setName("joe");
        account.setSecret(ValidationCode.generateSecret());

        String data = KeyUri.format(account.getName(), account.getSecret());

        System.out.println(data);

        FileOutputStream outputStream = new FileOutputStream(new File(QRCODE_FILE_NAME));

        Barcode.generate(data, outputStream);
    }


}
