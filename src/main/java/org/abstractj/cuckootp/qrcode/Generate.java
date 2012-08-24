package org.abstractj.cuckootp.qrcode;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.abstractj.cuckootp.model.Account;
import org.abstractj.cuckootp.token.TOTPUtils;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

public class Generate {

    private static int qrcodeWidth = 400;

    private static int qrcodeHeight = 400;

    private static String hostLabel = "brandonc.me";


    public static void main(String[] args) {

        Account account = new Account();
        account.setName("joe");
        account.setSecret(TOTPUtils.generateSecret());

        String data = getQRBarcodeURL(account.getName(), hostLabel, account.getSecret());

        BitMatrix matrix = null;
        QRCodeWriter writer = new QRCodeWriter();
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(2);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);
            MatrixToImageWriter.writeToFile(matrix, "PNG", new File("qrcode.png"));
        } catch (com.google.zxing.WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getQRBarcodeURL(String user, String host, String secret) {
        String format = "otpauth://totp/%s@%s?secret=%s";
        String formatted = String.format(format, user, host, secret);
        System.out.println(formatted);
        return formatted;
    }
}
