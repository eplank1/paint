package com.example.sprint1ms;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class ImageApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // --- Menu Bar ---
        MenuBar menuBar = new MenuBar();

        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open...");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As...");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(openItem, saveItem, saveAsItem, new SeparatorMenuItem(), exitItem);
        ToolBarManager toolBarHelper = new ToolBarManager();

        // Help menu
        Menu helpMenu = new Menu("Help");
        MenuItem helpItem = new MenuItem("Help");
        MenuItem aboutItem = new MenuItem("About");
        helpMenu.getItems().addAll(helpItem, aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);
        Easel easel1 = new Easel(toolBarHelper);

        // Scrollable image + canvas
        ScrollPane scrollPane = new ScrollPane();
        BorderPane imagePane = new BorderPane();
        //imagePane.getChildren().add(easel1.canvas);
        imagePane.setCenter(easel1.canvas);
        scrollPane.setContent(imagePane);

        root.setTop(menuBar);
        root.setBottom(toolBarHelper.toolBar);
        root.setCenter(scrollPane);

        FileManager fileManager = new FileManager();

        // --- Event handlers ---
        openItem.setOnAction(e -> fileManager.openImage(primaryStage, easel1));
        saveItem.setOnAction(e -> fileManager.saveImage(false, easel1));
        saveAsItem.setOnAction(e -> fileManager.saveImage(true, easel1));
        exitItem.setOnAction(e -> {
            if (fileManager.confirmUnsaved(easel1)) primaryStage.close();
        });

        aboutItem.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("Image Editor");
            alert.setContentText("A simple JavaFX paint-like editor.\nBuilt with JavaFX.");
            alert.showAndWait();
        });


        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Image Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}