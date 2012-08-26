/**
 * Copyright 2012 Bruno Oliveira, and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.abstractj.cuckootp.api.qrcode;

import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

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
