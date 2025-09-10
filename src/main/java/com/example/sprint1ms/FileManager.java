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
/*
* The FileManager Class is used as an intermediary between the stage and the easel.
 */
public class FileManager {
    /*
    * The default constructor for FileManger, used to initialize before using FileManager functions.
     */
    public FileManager() {


    }
    /*
    * The openImage function is used to show a file selection dialogue to stage and to
    * draw the image from said file onto the easel's canvas.
    *
    * @param stage The stage class is used to open a new tab for file selection.
    * @param easel The easel class is used to draw an image from a file onto the canvas.
     */
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
    /*
    * The saveImage function is used to select (or create) a file to save to
    * and write the canvas to said file.
    *
    * @param saveAs The boolean is used to determine whether to save to a preselected
    *               location or to open a saveAs dialogue window.
    * @param easel  The easel is used to create a snapshot of the canvas and write it
    *               to the selected file.
     */
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
    /*
    * The confirmUnsaved function is used to get confirmation from
    * the user if they want to close the program with unsaved changes.
    *
    * @param easel Used to retrieve isDirty flag from the easel.
     */
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
