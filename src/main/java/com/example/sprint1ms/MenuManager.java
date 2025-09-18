package com.example.sprint1ms;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
* The MenuManager class is used to create the menuBar that will give the user file options
 */
public class MenuManager {
    protected MenuBar menuBar;
    protected TabManager tabManager;
    protected Stage stage;
    protected ToolBarManager toolBarManager;

    /*
    * The default constructor for the MenuManager, creates the menubar and
    * adds event handlers to the buttons on the menu bar. A TabManager, Stage,
    * and ToolBarManager must be passed to add functionality to buttons.
    *
    * @param tabs          The TabManager is passed to MenuManager's property by reference
    * @param primaryStage  The Stage is passed to MenuManager's property by reference
    * @param toolBarHelper The ToolBarManager is passed to MenuManager's property by reference
     */
    public MenuManager(TabManager tabs, Stage primaryStage, ToolBarManager toolBarHelper) {
        // File Menu
        tabManager = tabs;
        stage = primaryStage;
        toolBarManager = toolBarHelper;

        menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem newTab = new MenuItem("New Tab");
        MenuItem openItem = new MenuItem("Open...");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As...");
        MenuItem undoItem = new MenuItem("Undo");
        MenuItem redoItem = new MenuItem("Redo");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(openItem, newTab, saveItem, saveAsItem,new SeparatorMenuItem(),undoItem, redoItem, new SeparatorMenuItem(), exitItem);
        // Help menu
        Menu helpMenu = new Menu("Help");
        MenuItem helpItem = new MenuItem("Help");
        MenuItem aboutItem = new MenuItem("About");
        helpMenu.getItems().addAll(helpItem, aboutItem);
        menuBar.getMenus().addAll(fileMenu, helpMenu);
        newTab.setOnAction(e -> tabManager.addNewTab("Untitled"));
        openItem.setOnAction(e -> openImage());
        saveItem.setOnAction(e -> saveImage(false));
        saveAsItem.setOnAction(e -> saveImage(true));
        exitItem.setOnAction(e -> {
            if (confirmUnsaved(tabManager.currentEasel)) primaryStage.close();
        });

        aboutItem.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("Image Editor");
            alert.setContentText("A simple paint-like editor.\nA way to espresso yourself.\nBuilt with JavaFX.");
            alert.showAndWait();
        });
        helpItem.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Help");
            alert.setHeaderText("Help with this Image Editor");
            alert.setContentText("The file menu allows you to open and save images.\nAt the bottom of the screen there are drawing tools to chose from.\nThere is also a dashed check-box to make your objects dashed if selected.");
            alert.showAndWait();
        });
        undoItem.setOnAction(e -> {if (!tabManager.currentEasel.undoStack.isEmpty()) undoChanges();});
        redoItem.setOnAction(e -> {if (!tabManager.currentEasel.redoStack.isEmpty()) redoChanges();});
    }

    /*
     * The openImage function is used to show a file selection dialogue to stage and to
     * draw the image from said file onto the easel's canvas on a new tab named after the file.
     *
     */
    public void openImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            Easel easel = tabManager.addNewTab(file.getName());
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
     */
    protected void saveImage(boolean saveAs) {
        Easel easel = tabManager.currentEasel;
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
    protected void undoChanges(){
        Easel easel = tabManager.currentEasel;
        easel.redoStack.push(easel.canvas.snapshot(null,null));
        easel.gc.drawImage(easel.undoStack.pop(), 0, 0);
    }
    protected void redoChanges(){
        Easel easel = tabManager.currentEasel;
        easel.undoStack.push(easel.canvas.snapshot(null,null));
        easel.gc.drawImage(easel.redoStack.pop(), 0, 0);
    }

}
