package com.example.sprint1ms;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javafx.application.Platform;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpHandling implements HttpHandler {
    protected Easel easel;

    public HttpHandling(Easel easel) {
        this.easel = easel;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        if (easel == null){
            String response = "There is no image available.";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            t.close();
            return;
        }
        Platform.runLater(() -> {
            try {
                BufferedImage bImage = PaintUtility.GetBufferedImage(easel.canvas);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bImage, "png", bos);
                byte[] image = bos.toByteArray();
                t.sendResponseHeaders(200, image.length);
                t.getResponseBody().write(image);
                t.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        });
    }
}
