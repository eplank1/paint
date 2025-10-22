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


/**
 * The type Menu manager.
 */
public class MenuManager {
    /**
     * The Menu bar.
     */
    protected MenuBar menuBar;
    /**
     * The Tab manager.
     */
    protected TabManager tabManager;
    /**
     * The Stage.
     */
    protected Stage stage;
    /**
     * The Tool bar manager.
     */
    protected ToolBarManager toolBarManager;
    /**
     * The Counter.
     */
    final int[] counter = {300};

    /**
     * Instantiates a new Menu manager.
     *
     * @param tabs          the tabs
     * @param primaryStage  the primary stage
     * @param toolBarHelper the tool bar helper
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
        //Image menu
        Menu imageMenu = new Menu("Image");
        MenuItem rotateItem = new MenuItem("Rotate");
        MenuItem verticalFlipItem = new MenuItem("Vertical Flip");
        MenuItem horizontalFlipItem = new MenuItem("Horizontal Flip");
        verticalFlipItem.setOnAction(e -> tabManager.currentEasel.verticalFlip());
        horizontalFlipItem.setOnAction(e -> tabManager.currentEasel.horizontalFlip());
        rotateItem.setOnAction(e -> tabManager.currentEasel.rotate());
        imageMenu.getItems().addAll(rotateItem, verticalFlipItem, horizontalFlipItem);

        helpMenu.getItems().addAll(helpItem, aboutItem);
        menuBar.getMenus().addAll(fileMenu, helpMenu, imageMenu);
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

    /**
     * Open image.
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
            Easel.originalFormat = PaintUtility.getFileExtension(file);
        }
        toolBarManager.logHelper.addLog("Image file, " + file + " opened");
    }

    /**
     * Save image.
     *
     * @param saveAs the save as flag
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
        Easel.convertFormat = PaintUtility.getFileExtension(file);
        if (confirmConvert(easel)) {

            try {
                // Create writable image the same size as the loaded image
                int width = (int) easel.canvas.getWidth();
                int height = (int) easel.canvas.getHeight();
                WritableImage writable = new WritableImage(width, height);

                easel.canvas.snapshot(null, writable);

                // Convert to BufferedImage for saving
                BufferedImage bImage = SwingFXUtils.fromFXImage(writable, null);

                ImageIO.write(bImage, "png", file);
                easel.isDirty = false;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        counter[0] = 300;
        toolBarManager.logHelper.addLog("Saving image to " + easel.currentFile);
    }

    /**
     * Confirm unsaved boolean.
     *
     * @param easel the easel
     * @return the boolean
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

    /**
     * Undo changes.
     */
    protected void undoChanges(){
        Easel easel = tabManager.currentEasel;
        easel.redoStack.push(easel.canvas.snapshot(null,null));
        //handling for draw -> rotate -> undo
        easel.canvas.setHeight(easel.undoStack.peek().getHeight());
        easel.canvas.setWidth(easel.undoStack.peek().getWidth());
        easel.gc.drawImage(easel.undoStack.pop(), 0, 0);
        toolBarManager.logHelper.addLog("Undoing changes.");
    }

    /**
     * Redo changes.
     */
    protected void redoChanges(){
        Easel easel = tabManager.currentEasel;
        easel.undoStack.push(easel.canvas.snapshot(null,null));
        //handling for draw -> rotate -> undo
        easel.canvas.setHeight(easel.redoStack.peek().getHeight());
        easel.canvas.setWidth(easel.redoStack.peek().getWidth());
        easel.gc.drawImage(easel.redoStack.pop(), 0, 0);
        toolBarManager.logHelper.addLog("Redoing changes.");
    }

    /**
     * Confirm convert boolean.
     *
     * @param easel the easel
     * @return the boolean
     */
    protected boolean confirmConvert(Easel easel){
        Boolean isConfirmed = false;
        if ("png".equalsIgnoreCase(easel.originalFormat)) return true;
        else if ((easel.originalFormat.equals("jpg") || easel.originalFormat.equals("jpeg") || easel.originalFormat.equals("bmp"))
                && easel.convertFormat.equals("png")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "The current format you are saving as will involve loss. Do you want to continue?", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText("Converting Formats Warning");
            alert.showAndWait();
            isConfirmed = (alert.getResult() == ButtonType.YES);
            return isConfirmed;
        }
        return isConfirmed;
    }
}
