package com.example.sprint1ms;

import javafx.scene.control.*;
import javafx.stage.Stage;

public class MenuManager {
    protected MenuBar menuBar;

    public MenuManager(FileManager fileManager, TabManager tabManager, Stage primaryStage, ToolBarManager toolBarHelper) {
        // File Menu
        menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem newTab = new MenuItem("New Tab");
        MenuItem openItem = new MenuItem("Open...");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As...");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(openItem, newTab, saveItem, saveAsItem, new SeparatorMenuItem(), exitItem);
        // Help menu
        Menu helpMenu = new Menu("Help");
        MenuItem helpItem = new MenuItem("Help");
        MenuItem aboutItem = new MenuItem("About");
        helpMenu.getItems().addAll(helpItem, aboutItem);
        menuBar.getMenus().addAll(fileMenu, helpMenu);
        newTab.setOnAction(e -> tabManager.addNewTab(toolBarHelper,"Untitled"));
        openItem.setOnAction(e -> fileManager.openImage(primaryStage, tabManager.currentEasel));
        saveItem.setOnAction(e -> fileManager.saveImage(false, tabManager.currentEasel));
        saveAsItem.setOnAction(e -> fileManager.saveImage(true, tabManager.currentEasel));
        exitItem.setOnAction(e -> {
            if (fileManager.confirmUnsaved(tabManager.currentEasel)) primaryStage.close();
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
            alert.setContentText("The file menu allows you to open and save images.\nAt the bottom of the screen there are drawing tools to chose from." +
                    "\nThere is also a dashed check-box to make your objects dashed if selected.");
            alert.showAndWait();
        });
    }

}
