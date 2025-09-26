package com.example.sprint1ms;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javafx.scene.canvas.Canvas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
class MenuManagerTest{
    @BeforeAll
    static void initJfx() {
        new JFXPanel();
    }
    private void runAndWait(Runnable action) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        });
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("Timeout waiting for FX task");
        }
    }

    @Test
    void testGetFileExtension_withJPG() throws Exception {
        runAndWait(() -> {
            File file = new File("testing.jpg");
            String extension = PaintUtility.getFileExtension(file);
            assertEquals("jpg", extension);
        });

    }
    @Test
    void testGetFileExtension_noExtensionDefaultsToPng() throws Exception {
        runAndWait(() -> {
            File file = new File("testImage");
            String ext = PaintUtility.getFileExtension(file);
            assertEquals("png", ext);
        });

    }
    @Test
    void testMakeBufferedImage() throws Exception{
        runAndWait(() -> {
            Canvas canvas = new Canvas();
            BufferedImage test = PaintUtility.GetBufferedImage(canvas);
            assertNotNull(test);
        });
    }

}