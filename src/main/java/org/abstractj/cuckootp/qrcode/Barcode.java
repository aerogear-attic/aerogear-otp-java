package org.abstractj.cuckootp.qrcode;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.IOException;

public class Barcode {

    private static final int QRCODE_WIDTH = 400;
    private static final int QRCODE_HEIGHT = 400;
    public static final String QRCODE_FILE_NAME = "qrcode.png";
    public static final String IMAGE_FORMAT = "PNG";

    public static void generate(String data) {

        BitMatrix matrix = null;
        QRCodeWriter writer = new QRCodeWriter();
        try {
            matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_HEIGHT, Hints.create());
            MatrixToImageWriter.writeToFile(matrix, IMAGE_FORMAT, new File(QRCODE_FILE_NAME));
        } catch (com.google.zxing.WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
