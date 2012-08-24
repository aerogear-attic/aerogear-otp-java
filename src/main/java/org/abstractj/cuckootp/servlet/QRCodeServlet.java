package org.abstractj.cuckootp.servlet;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.abstractj.cuckootp.model.Account;
import org.abstractj.cuckootp.qrcode.Barcode;
import org.abstractj.cuckootp.totp.KeyUri;
import org.abstractj.cuckootp.totp.TOTPUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/test")
public class QRCodeServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");

        Account account = new Account();
        account.setName("joe");
        account.setSecret(TOTPUtils.generateSecret());

        String data = KeyUri.format(account.getName(), account.getSecret());

        Barcode.generate(data);
    }
}
