package com.example.sprint1ms;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileManager {

    public FileManager() {


    }

    public void openImage(Stage stage, Easel easel) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            easel.canvas.setHeight(image.getHeight());
            easel.canvas.setWidth(image.getWidth());
            easel.gc = easel.canvas.getGraphicsContext2D();
            easel.gc.drawImage(image, 0,0);
            easel.currentFile = file;
            easel.isDirty = false;
        }
    }

    protected void saveImage(boolean saveAs, Easel easel) {
        if (!easel.isDirty) return;

        File file = easel.currentFile;

        if (saveAs || file == null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                    new FileChooser.ExtensionFilter("JPG Files", "*.jpg"),
                    new FileChooser.ExtensionFilter("BMP Files", "*.bmp")
            );
            file = fileChooser.showSaveDialog(null);
            if (file == null) return;
            easel.currentFile = file;
        }

        try {
            // Create writable image the same size as the loaded image
            int width = (int) easel.canvas.getWidth();
            int height = (int) easel.canvas.getHeight();
            WritableImage writable = new WritableImage(width, height);

            // Then draw easel.canvas edits on top
            easel.canvas.snapshot(null, writable);

            // Convert to BufferedImage for saving
            BufferedImage bImage = SwingFXUtils.fromFXImage(writable, null);


            ImageIO.write(bImage, "png", file);
            easel.isDirty = false;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected boolean confirmUnsaved(Easel easel) {
        if (!easel.isDirty) return true;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "You have unsaved changes. Do you want to exit?",
                ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Unsaved Changes");
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }


}
