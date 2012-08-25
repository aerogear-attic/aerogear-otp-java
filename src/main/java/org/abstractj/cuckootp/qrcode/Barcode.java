package org.abstractj.cuckootp.qrcode;

import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static com.google.zxing.BarcodeFormat.QR_CODE;

public class Barcode {

    private static final int QRCODE_WIDTH = 400;
    private static final int QRCODE_HEIGHT = 400;
    public static final String IMAGE_FORMAT = "PNG";

    public static void generate(String data, OutputStream outputStream) {

        BitMatrix matrix = null;
        com.google.zxing.Writer writer = new MultiFormatWriter();
        try {
            matrix = writer.encode(data, QR_CODE, QRCODE_WIDTH, QRCODE_HEIGHT, Hints.create());
            MatrixToImageWriter.writeToStream(matrix, IMAGE_FORMAT, outputStream);
        } catch (com.google.zxing.WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
