package com.example.sprint1ms;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.canvas.Canvas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class PaintUtility {
    public static String getFileExtension(File file){
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex+1).toLowerCase();
        }
        return "png";//by default return png format
    }
    /*
     * Creates a ToggleButton using a button name, tool from the toolbar, toggle-group and a boolean.
     * @param text      The text string applied to the button.
     * @param tool      The tool that the button affects/activates.
     * @param group     The toggle group that the button must be applied to
     * @param selected  Boolean to distinguish whether the button is currently selected or not.
     */
    static public ToggleButton makeToolButton(String text, ToolBarManager.Tool tool, ToggleGroup group, boolean selected, ToolBarManager toolBarManager, String path) {
        ToggleButton btn = new ToggleButton(text);
        javafx.scene.image.Image img = new Image(path);
        ImageView imageView = new ImageView(img);
        btn.setGraphic(imageView);
        btn.setToggleGroup(group);
        btn.setSelected(selected);
        if (selected) toolBarManager.currentTool = tool;
        btn.setOnAction(e -> toolBarManager.currentTool = tool);
        return btn;
    }
    static public BufferedImage GetBufferedImage(Canvas canvas) {
        WritableImage writableImage = canvas.snapshot(null, null);
        return SwingFXUtils.fromFXImage(writableImage, null);
    }
}
